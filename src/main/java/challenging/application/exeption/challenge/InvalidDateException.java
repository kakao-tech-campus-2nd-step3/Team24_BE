package challenging.application.exeption.challenge;

public class InvalidDateException extends RuntimeException {

  public InvalidDateException(String message) {
    super(message);
  }

}
