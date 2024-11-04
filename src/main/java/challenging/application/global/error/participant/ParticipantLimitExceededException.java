package challenging.application.global.error.participant;

import challenging.application.global.error.ErrorCode;

public class ParticipantLimitExceededException extends RuntimeException {
  private final ErrorCode errorCode;

  public ParticipantLimitExceededException(ErrorCode errorCode) {
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
