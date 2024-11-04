package challenging.application.global.exception.challenge;

import challenging.application.global.exception.ExceptionMessage;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {
    super(ExceptionMessage.USER_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
