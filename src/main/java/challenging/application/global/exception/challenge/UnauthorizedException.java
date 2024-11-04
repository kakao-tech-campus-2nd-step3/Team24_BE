package challenging.application.global.exception.challenge;

import challenging.application.global.exception.ExceptionMessage;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException() {
    super(ExceptionMessage.UNAUTHORIZED_EXCEPTION);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
