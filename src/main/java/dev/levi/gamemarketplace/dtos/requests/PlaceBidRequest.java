package dev.levi.gamemarketplace.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlaceBidRequest(
        @NotNull(message = "Bid amount is required")
        @Positive(message = "Bid amount must be greater than zero")
        Long amount
) {}
