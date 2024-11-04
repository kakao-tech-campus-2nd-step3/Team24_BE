package challenging.application.global.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCode {
    //AUTH
    UNAUTHORIZED_USER_ERROR(HttpStatus.UNAUTHORIZED, "인증 되지 않은 사용자 입니다."),
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다. 유저를 찾을 수 없습니다."),
    TOKEN_EXPIRED_ERROR(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
    TOKEN_MALFORMED_ERROR(HttpStatus.BAD_REQUEST, "올바른 Token 형식이 아닙니다."),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.BAD_REQUEST, "지원하지 않는 Token 입니다."),
    TOKEN_SIGNATURE_ERROR(HttpStatus.BAD_REQUEST, "Token 의 서명이 유효하지 않습니다."),
    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "잘못된 Token 입니다."),

    //HISTORY
    HISTORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재 하지 않는 히스토리 입니다."),

    //CHALLENGE
    CHALLENGE_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "존재 하지 않는 챌린지 입니다."),
    CHALLENGE_UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "챌린지를 삭제할 권한이 없습니다."),
    CHALLENGE_ALREADY_RESERVED_ERROR(HttpStatus.BAD_REQUEST, "이미 예약한 챌린지입니다."),

    //DATE
    DATE_INVALID_ERROR(HttpStatus.BAD_REQUEST, "날짜가 유효하지 않습니다."),

    //CATEGORY
    CATEGORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 입니다."),

    //PARTICIPANT
    PARTICIPANT_LIMIT_ERROR(HttpStatus.BAD_REQUEST, "최대 참가자 수에 도달했습니다.");

    private final HttpStatus status;
    private final String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}