package challenging.application.global.error.userprofile;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class PointNotNegetiveException extends Team24Exception {
    public PointNotNegetiveException() {
        super(ErrorCode.POINT_NOT_NEGETIVE_ERROR);
    }
}
