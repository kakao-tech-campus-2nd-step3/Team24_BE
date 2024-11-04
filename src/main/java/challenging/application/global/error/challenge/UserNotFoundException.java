package challenging.application.global.error.challenge;

import challenging.application.global.error.ExceptionMessage;

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
