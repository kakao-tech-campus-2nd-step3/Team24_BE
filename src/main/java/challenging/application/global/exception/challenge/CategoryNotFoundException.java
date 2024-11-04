package challenging.application.global.exception.challenge;

import challenging.application.global.exception.ExceptionMessage;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(String message) {
    super(message);
  }

  public CategoryNotFoundException() {
    super(ExceptionMessage.CATEGORY_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
