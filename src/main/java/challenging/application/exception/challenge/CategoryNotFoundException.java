package challenging.application.exception.challenge;

import static challenging.application.exception.ExceptionMessage.CATEGORY_NOT_FOUND;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException(String message) {
    super(message);
  }

  public CategoryNotFoundException() {
    super(CATEGORY_NOT_FOUND);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
