package dev.levi.gamemarketplace.error.exceptions;

import java.util.UUID;

public class AuctionExpiredException extends RuntimeException {
    public AuctionExpiredException(UUID auctionId) {
        super("Auction " + auctionId + " has already expired");
    }
}
