package services;

public class InvalidAddress extends RuntimeException {
  public InvalidAddress(String message) {
    super(message);
  }
}
