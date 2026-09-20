package dev.levi.gamemarketplace.services;

import dev.levi.gamemarketplace.dtos.requests.CreateAuctionRequest;
import dev.levi.gamemarketplace.entities.Auction;
import dev.levi.gamemarketplace.entities.Bid;
import dev.levi.gamemarketplace.entities.Item;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.enums.AuctionStatus;
import dev.levi.gamemarketplace.error.exceptions.AuctionNotActiveException;
import dev.levi.gamemarketplace.error.exceptions.ResourceNotFoundException;
import dev.levi.gamemarketplace.redis.AuctionExpirationPublisher;
import dev.levi.gamemarketplace.repositories.AuctionRepository;
import dev.levi.gamemarketplace.repositories.BidRepository;
import dev.levi.gamemarketplace.repositories.ItemRepository;
import dev.levi.gamemarketplace.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final AuctionExpirationPublisher auctionExpirationPublisher;

    @Transactional
    public Bid placeBid(UUID auctionId, UUID bidderId, Long amount) {
        // 1. Pessimist locking (SELECT ... FOR UPDATE) to assure bid order
        Auction auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + auctionId));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new AuctionNotActiveException("Auction is not active or has already expired.");
        }
        if (auction.getEndsAt().isBefore(Instant.now())) {
            throw new AuctionExpiredException("Auction is not active or has already expired.");
        }

        if (auction.getSeller().getId().equals(bidderId)) {
            throw new IllegalArgumentException("You cannot bid on your own auction.");
        }

        // 2. Get the highest bid after locking row
        var currentHighest = auction.getCurrentPrice() != null ? auction.getCurrentPrice() : 100;
        if (amount <= currentHighest) {
            throw new BidTooLowException(amount, currentHighest)
            );
        }

        Player bidder = playerRepository.findById(bidderId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + bidderId));

        if (bidder.getGoldBalance() < amount) {
            throw new IllegalStateException("Insufficient gold balance.");
        }

        // 3. Reimburses old highest bidder
        bidRepository.findTopByAuctionIdOrderByAmountDesc(auctionId).ifPresent(previousHighestBid -> {
            Player previousBidder = previousHighestBid.getBidder();

            previousBidder.setGoldBalance(previousBidder.getGoldBalance() + previousHighestBid.getAmount());
            playerRepository.save(previousBidder);

            log.info("Refunded {} gold to player {} on auction {}",
                    previousHighestBid.getAmount(), previousBidder.getId(), auctionId);
        });

        bidder.setGoldBalance(bidder.getGoldBalance() - amount);
        playerRepository.save(bidder);

        auction.setCurrentPrice(amount);
        auctionRepository.save(auction);

        var bid = new Bid(auction, bidder, amount);
        log.info("Player {} successfully placed a bid of {} gold on auction {}", bidderId, amount, auctionId);
        return bid;
    }

    @Transactional
    public Auction createAuction(UUID sellerId, CreateAuctionRequest request) {
        Player seller = playerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + sellerId));

        var item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + request.itemId()));

        if (!seller.getId().equals(item.getOwner().getId())) {
            throw new IllegalArgumentException("You can only create an auction for an item you own.");
        }

        item.setOwner(null);
        itemRepository.save(item);

        var endsAt = LocalDateTime.now().plusMinutes(request.durationInMinutes());
        var auction = new Auction(
                item,
                seller,
                request.initialPrice(),
                endsAt.toInstant(ZoneOffset.UTC)
        );
        auctionRepository.save(auction);
        auctionExpirationPublisher.scheduleAuctionClose(auction.getId(), auction.getEndsAt());

        return auction;
    }

    @Transactional
    public void closeAuction(UUID auctionId) {
        var auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + auctionId));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            return;
        }

        bidRepository.findTopByAuctionIdOrderByAmountDesc(auctionId)
                .ifPresentOrElse(
                        highestBid -> processSuccessfulAuction(auction, highestBid),
                        () -> processUnsoldAuction(auction)
                );
    }

    private void processSuccessfulAuction(Auction auction, Bid highestBid) {
        var seller = auction.getSeller();
        var winner = highestBid.getBidder();
        var item = auction.getItem();

        seller.setGoldBalance(seller.getGoldBalance() + highestBid.getAmount());
        item.setOwner(winner);
        auction.setStatus(AuctionStatus.CLOSED);
        auction.setWinner(winner.getId());

        playerRepository.save(seller);
        itemRepository.save(item);
        auctionRepository.save(auction);

        log.info("Auction {} closed successfully. Winner: {}, Item: {}",
                auction.getId(), winner.getId(), item.getId());
    }

    private void processUnsoldAuction(Auction auction) {
        Item item = auction.getItem();
        item.setOwner(auction.getSeller());
        auction.setStatus(AuctionStatus.CLOSED);

        itemRepository.save(item);
        auctionRepository.save(auction);

        log.info("Auction {} expired without bids. Item returned to seller {}",
                auction.getId(), auction.getSeller().getId());
    }

    @Transactional(readOnly = true)
    public Page<Auction> getActiveAuctions(Pageable pageable) {
        return auctionRepository.findNonExpiredActiveAuctions(Instant.now(), pageable);
    }

    @Transactional(readOnly = true)
    public Auction getAuctionById(UUID id) {
        return auctionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found with id: " + id));
    }
}