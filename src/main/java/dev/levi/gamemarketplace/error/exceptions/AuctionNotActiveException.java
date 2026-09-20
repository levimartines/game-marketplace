package dev.levi.gamemarketplace.error.exceptions;

import java.util.UUID;

public class AuctionNotActiveException extends RuntimeException {
    public AuctionNotActiveException(UUID auctionId) {
        super("Auction " + auctionId + " is not active");
    }
}
