package dev.levi.gamemarketplace.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateAuctionRequest(
        @NotNull(message = "Item ID is required")
        UUID itemId,

        @NotNull(message = "Initial price is required")
        @Positive(message = "Initial price must be greater than zero")
        Long initialPrice,

        @NotNull(message = "Duration in minutes is required")
        @Positive(message = "Duration must be at least 1 minute")
        Integer durationInMinutes
) {
}
