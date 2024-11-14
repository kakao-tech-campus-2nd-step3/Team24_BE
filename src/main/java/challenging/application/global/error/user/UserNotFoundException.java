package challenging.application.global.error.user;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class UserNotFoundException extends Team24Exception {
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND_ERROR);
  }

}
