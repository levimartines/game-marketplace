package dev.levi.gamemarketplace.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record RewardClaimResponse(
        UUID id,
        UUID itemId,
        String itemType,
        String itemRarity,
        Long goldAmount,
        Instant claimedAt,
        Instant nextClaimAvailableAt
) {
}