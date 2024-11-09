package challenging.application.global.error.category;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class CategoryNotFoundException extends Team24Exception {
  public CategoryNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
