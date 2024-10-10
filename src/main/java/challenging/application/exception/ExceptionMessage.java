package challenging.application.exception;

public class ExceptionMessage {
    public static final String UNAUTHORIZED_USER = "인증 되지 않은 사용자 입니다.";
    public static final String EXPIRED_JWT_EXCEPTION = "토큰이 만료되었습니다.";
    public static final String MALFORMED_JWT_EXCEPTION = "올바른 Token 형식이 아닙니다.";
    public static final String UNSUPPORTED_JWT_EXCEPTION = "지원하지 않는 Token 입니다.";
    public static final String SIGNATURE_EXCEPTION = "Token 의 서명이 유효하지 않습니다.";
    public static final String JWT_EXCEPTION = "잘못된 Token 입니다.";
    public static final String HISTORY_NOT_FOUND = "존재 하지 않는 히스토리 입니다.";
    public static final String CHALLENGE_NOT_FOUND = "존재 하지 않는 챌린지 입니다.";
    public static final String INVALID_DATE = "날짜가 유효하지 않습니다.";
    public static final String CATEGORY_NOT_FOUND = "옳지 않은 카테고리 ID 입니다.";
    public static final String USER_NOT_FOUND = "해당 유저를 찾을 수 없습니다.";
}
