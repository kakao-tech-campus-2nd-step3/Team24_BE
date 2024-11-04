package challenging.application.global.error.category;

import challenging.application.global.error.ErrorCode;

public class CategoryNotFoundException extends RuntimeException {
  private final ErrorCode errorCode;

  public CategoryNotFoundException(ErrorCode errorCode) {
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
