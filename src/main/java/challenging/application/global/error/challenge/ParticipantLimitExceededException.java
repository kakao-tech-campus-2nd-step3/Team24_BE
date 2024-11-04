package challenging.application.global.error.challenge;

import challenging.application.global.error.ExceptionMessage;

public class ParticipantLimitExceededException extends RuntimeException {
  public ParticipantLimitExceededException() {
    super(ExceptionMessage.PARTICIPANT_LIMIT_EXCEEDED);
  }

  public ParticipantLimitExceededException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
