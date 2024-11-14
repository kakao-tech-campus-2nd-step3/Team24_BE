package challenging.application.global.error.user;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class UnauthorizedException extends Team24Exception {

  public UnauthorizedException() {
    super(ErrorCode.UNAUTHORIZED_USER_ERROR);
  }

}
