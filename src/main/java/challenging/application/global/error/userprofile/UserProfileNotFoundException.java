package challenging.application.global.error.userprofile;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class UserProfileNotFoundException extends Team24Exception {
    public UserProfileNotFoundException() {
        super(ErrorCode.USER_PROFILE_NOT_FOUND_ERROR);
    }
}
