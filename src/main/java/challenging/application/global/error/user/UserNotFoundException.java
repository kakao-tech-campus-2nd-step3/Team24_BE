package challenging.application.global.error.user;

import challenging.application.global.error.ErrorCode;

public class UserNotFoundException extends RuntimeException {
  private final ErrorCode errorCode;

  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

  public int getStatusCode(){
    return errorCode.getStatus().value();
  }
}
