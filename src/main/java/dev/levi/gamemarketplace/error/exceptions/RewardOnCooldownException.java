package dev.levi.gamemarketplace.error.exceptions;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RewardOnCooldownException extends RuntimeException {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public RewardOnCooldownException(UUID playerId, Instant lastClaim, Duration cooldown) {
        super(String.format(
                "Player '%s' already claimed a reward at %s and is on cooldown for %s minutes.",
                playerId, LocalDateTime.ofInstant(lastClaim, ZoneOffset.UTC).format(FORMATTER), cooldown.toMinutes()
        ));
    }

}