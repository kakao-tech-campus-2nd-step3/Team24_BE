package challenging.application.global.error.user;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class ForbiddenException extends Team24Exception {

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN_ERROR);
    }
}
