package dev.levi.gamemarketplace.dtos.responses;

import dev.levi.gamemarketplace.enums.AuctionStatus;

import java.time.Instant;
import java.util.UUID;

public record AuctionResponse(
        UUID id,
        UUID itemId,
        String itemType,
        UUID sellerId,
        String sellerUsername,
        Long startingPrice,
        Long currentPrice,
        AuctionStatus status,
        Instant createdAt,
        Instant endsAt
) {
}