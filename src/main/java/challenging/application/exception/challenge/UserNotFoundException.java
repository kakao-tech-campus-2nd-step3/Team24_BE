package challenging.application.exception.challenge;

import static challenging.application.exception.ExceptionMessage.USER_NOT_FOUND;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {
    super(USER_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
