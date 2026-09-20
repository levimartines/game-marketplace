package dev.levi.gamemarketplace.error.exceptions;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String field, String value) {
        super("Player with " + field + " '" + value + "' already exists");
    }
}