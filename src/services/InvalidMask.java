package services;

public class InvalidMask extends RuntimeException {
    public InvalidMask(String message) {
        super(message);
    }
}
