package challenging.application.global.exception.challenge;

import challenging.application.global.exception.ExceptionMessage;

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
