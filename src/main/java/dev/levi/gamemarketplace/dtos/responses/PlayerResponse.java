package dev.levi.gamemarketplace.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record PlayerResponse(
        UUID id,
        String username,
        String email,
        Long goldBalance,
        Instant createdAt
) {
}
