package challenging.application.global.error.challenge;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ParticipantNotFoundException extends Team24Exception {
  public ParticipantNotFoundException() {
    super(ErrorCode.PARTICIPANT_NOT_FOUND_ERROR);
  }
}
