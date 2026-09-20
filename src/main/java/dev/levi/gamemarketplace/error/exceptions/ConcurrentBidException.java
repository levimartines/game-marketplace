package dev.levi.gamemarketplace.error.exceptions;

import java.util.UUID;

public class ConcurrentBidException extends RuntimeException {
    public ConcurrentBidException(UUID auctionId) {
        super("Another bid was placed concurrently on auction " + auctionId + ". Please retry.");
    }
}