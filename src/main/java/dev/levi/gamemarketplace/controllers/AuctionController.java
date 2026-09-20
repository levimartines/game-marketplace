package dev.levi.gamemarketplace.controllers;

import dev.levi.gamemarketplace.dtos.requests.CreateAuctionRequest;
import dev.levi.gamemarketplace.dtos.requests.PlaceBidRequest;
import dev.levi.gamemarketplace.dtos.responses.AuctionResponse;
import dev.levi.gamemarketplace.dtos.responses.BidResponse;
import dev.levi.gamemarketplace.entities.Auction;
import dev.levi.gamemarketplace.entities.Bid;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.mappers.AuctionMapper;
import dev.levi.gamemarketplace.services.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionMapper auctionMapper;

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(
            @Valid @RequestBody CreateAuctionRequest request,
            @AuthenticationPrincipal Player authenticatedPlayer
    ) {
        Auction auction = auctionService.createAuction(authenticatedPlayer.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(auctionMapper.toAuctionResponse(auction));
    }

    @GetMapping
    public ResponseEntity<PagedModel<AuctionResponse>> getActiveAuctions(
            @PageableDefault(size = 20, sort = "endsAt") Pageable pageable
    ) {
        Page<AuctionResponse> response = auctionService.getActiveAuctions(pageable)
                .map(auctionMapper::toAuctionResponse);
        return ResponseEntity.ok(new PagedModel<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable UUID id) {
        Auction auction = auctionService.getAuctionById(id);
        return ResponseEntity.ok(auctionMapper.toAuctionResponse(auction));
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable UUID id,
            @Valid @RequestBody PlaceBidRequest request,
            @AuthenticationPrincipal Player authenticatedPlayer
    ) {
        Bid bid = auctionService.placeBid(id, authenticatedPlayer.getId(), request.amount());
        return ResponseEntity.ok(auctionMapper.toBidResponse(bid));
    }

}
