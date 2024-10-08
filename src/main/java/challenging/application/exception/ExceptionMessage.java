package challenging.application.exception;

public class ExceptionMessage {

    public static final String EXPIRED_JWT_EXCEPTION = "토큰이 만료되었습니다.";
    public static final String MALFORMED_JWT_EXCEPTION = "올바른 Token 형식이 아닙니다.";
    public static final String UNSUPPORTED_JWT_EXCEPTION = "지원하지 않는 Token 입니다.";
    public static final String SIGNATURE_EXCEPTION = "Token 의 서명이 유효하지 않습니다.";
    public static final String JWT_EXCEPTION = "잘못된 Token 입니다.";
    public static final String HISTORY_NOT_FOUND = "존재 하지 않는 히스토리 입니다.";
}
