package challenging.application.global.exception.challenge;

import challenging.application.global.exception.ExceptionMessage;

public class HistoryNotFoundException extends RuntimeException {

  public HistoryNotFoundException(String message) {
    super(message);
  }

  public HistoryNotFoundException() {
    super(ExceptionMessage.HISTORY_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
