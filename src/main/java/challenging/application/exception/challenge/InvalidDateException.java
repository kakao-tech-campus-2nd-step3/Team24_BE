package challenging.application.exception.challenge;

import static challenging.application.exception.ExceptionMessage.INVALID_DATE;

public class InvalidDateException extends RuntimeException {

  public InvalidDateException(String message) {
    super(message);
  }

  public InvalidDateException() {
    super(INVALID_DATE);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
