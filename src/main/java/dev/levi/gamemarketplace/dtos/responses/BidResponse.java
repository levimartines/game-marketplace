package dev.levi.gamemarketplace.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record BidResponse(
        UUID id,
        UUID auctionId,
        UUID bidderId,
        String bidderUsername,
        Long amount,
        Instant createdAt
) {}
