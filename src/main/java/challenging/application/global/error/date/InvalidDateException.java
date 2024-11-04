package challenging.application.global.error.date;

import challenging.application.global.error.ErrorCode;

public class InvalidDateException extends RuntimeException {
  private final ErrorCode errorCode;

  public InvalidDateException(ErrorCode errorCode) {
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
