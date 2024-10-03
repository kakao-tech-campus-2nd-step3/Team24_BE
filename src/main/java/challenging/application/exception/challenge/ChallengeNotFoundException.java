package challenging.application.exception.challenge;

public class ChallengeNotFoundException extends RuntimeException {

  public ChallengeNotFoundException(String message) {
    super(message);
  }
}
