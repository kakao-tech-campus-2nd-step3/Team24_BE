package challenging.application.global.error.history;

import challenging.application.global.error.ErrorCode;

public class HistoryNotFoundException extends RuntimeException {
  private final ErrorCode errorCode;

  public HistoryNotFoundException(ErrorCode errorCode) {
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
