package challenging.application.global.error.userprofile;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class PointNotEnoughException extends Team24Exception {
    public PointNotEnoughException() {
        super(ErrorCode.POINT_NOT_ENOUGH_ERROR);
    }
}
