package challenging.application.global.error.challenge;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class AlreadyReservedException extends Team24Exception {
  public AlreadyReservedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
