package dev.levi.gamemarketplace.repositories;

import dev.levi.gamemarketplace.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(UUID auctionId);
}
