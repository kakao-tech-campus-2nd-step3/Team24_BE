package challenging.application.exception.challenge;

import challenging.application.exception.ExceptionMessage;

import static challenging.application.exception.ExceptionMessage.*;

public class HistoryNotFoundException extends RuntimeException {

  public HistoryNotFoundException(String message) {
    super(message);
  }

  public HistoryNotFoundException() {
    super(HISTORY_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
