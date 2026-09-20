package dev.levi.gamemarketplace.error.exceptions;

public class BidTooLowException extends RuntimeException {
    public BidTooLowException(Long bidAmount, Long currentPrice) {
        super("Bid " + bidAmount + " must be higher than current price " + currentPrice);
    }
}
