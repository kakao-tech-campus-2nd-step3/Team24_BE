package challenging.application.exception.challenge;
import static challenging.application.exception.ExceptionMessage.CHALLENGE_NOT_FOUND;
public class ChallengeNotFoundException extends RuntimeException {

  public ChallengeNotFoundException(String message) {
    super(message);
  }

  public ChallengeNotFoundException() {
    super(CHALLENGE_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
