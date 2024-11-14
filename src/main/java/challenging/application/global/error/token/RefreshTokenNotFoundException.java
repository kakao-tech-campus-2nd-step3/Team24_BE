package challenging.application.global.error.token;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.Team24Exception;

public class RefreshTokenNotFoundException extends Team24Exception {
    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}
