package challenging.application.global.error.challenge;

import challenging.application.global.error.ErrorCode;

public class AlreadyReservedException extends RuntimeException {
  private final ErrorCode errorCode;

  public AlreadyReservedException(ErrorCode errorCode) {
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
