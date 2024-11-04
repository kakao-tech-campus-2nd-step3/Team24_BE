package challenging.application.global.error.challenge;

import challenging.application.global.error.ExceptionMessage;

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
