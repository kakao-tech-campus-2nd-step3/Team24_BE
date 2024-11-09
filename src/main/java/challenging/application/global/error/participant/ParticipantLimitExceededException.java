package challenging.application.global.error.participant;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ParticipantLimitExceededException extends Team24Exception {
  public ParticipantLimitExceededException(ErrorCode errorCode) {
    super(errorCode);
  }
}
