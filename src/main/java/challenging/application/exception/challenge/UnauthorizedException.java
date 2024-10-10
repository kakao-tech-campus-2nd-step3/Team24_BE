package challenging.application.exception.challenge;

import static challenging.application.exception.ExceptionMessage.UNAUTHORIZED_EXCEPTION;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException() {
    super(UNAUTHORIZED_EXCEPTION);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
