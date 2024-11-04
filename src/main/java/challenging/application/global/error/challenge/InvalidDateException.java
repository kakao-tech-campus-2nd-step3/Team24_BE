package challenging.application.global.error.challenge;

import challenging.application.global.error.ExceptionMessage;

public class InvalidDateException extends RuntimeException {

  public InvalidDateException(String message) {
    super(message);
  }

  public InvalidDateException() {
    super(ExceptionMessage.INVALID_DATE);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
