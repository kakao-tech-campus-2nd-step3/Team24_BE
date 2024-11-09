package challenging.application.global.error.date;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class InvalidDateException extends Team24Exception {
  public InvalidDateException(ErrorCode errorCode) {
    super(errorCode);
  }
}
