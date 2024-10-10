package challenging.application.exception.challenge;

import static challenging.application.exception.ExceptionMessage.*;

public class ParticipantLimitExceededException extends RuntimeException {
  public ParticipantLimitExceededException() {
    super(PARTICIPANT_LIMIT_EXCEEDED);
  }

  public ParticipantLimitExceededException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
