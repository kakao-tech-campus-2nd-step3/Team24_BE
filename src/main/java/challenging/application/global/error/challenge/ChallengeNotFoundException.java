package challenging.application.global.error.challenge;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ChallengeNotFoundException extends Team24Exception {
  public ChallengeNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
