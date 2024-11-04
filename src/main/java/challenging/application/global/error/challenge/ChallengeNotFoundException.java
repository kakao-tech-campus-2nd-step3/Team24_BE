package challenging.application.global.error.challenge;

import challenging.application.global.error.ExceptionMessage;

public class ChallengeNotFoundException extends RuntimeException {

  public ChallengeNotFoundException(String message) {
    super(message);
  }

  public ChallengeNotFoundException() {
    super(ExceptionMessage.CHALLENGE_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
