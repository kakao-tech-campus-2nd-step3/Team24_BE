package challenging.application.global.exception.challenge;

import challenging.application.global.exception.ExceptionMessage;

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
