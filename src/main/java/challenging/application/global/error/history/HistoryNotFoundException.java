package challenging.application.global.error.history;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class HistoryNotFoundException extends Team24Exception {
  public HistoryNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
