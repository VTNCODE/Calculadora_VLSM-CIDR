package services;

public class ExceededHosts extends RuntimeException {
    public ExceededHosts(String message) {
        super(message);
    }
}
