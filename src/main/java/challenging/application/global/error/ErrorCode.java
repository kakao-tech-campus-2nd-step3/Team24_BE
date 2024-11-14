package challenging.application.global.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCode {
    //AUTH
    UNAUTHORIZED_USER_ERROR(HttpStatus.UNAUTHORIZED, "인증 되지 않은 사용자 입니다."),
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다. 유저를 찾을 수 없습니다."),

    //Token
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, "올바른 Token 형식이 아닙니다."),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.UNAUTHORIZED, "지원하지 않는 Token 입니다."),
    TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "Token 의 서명이 유효하지 않습니다."),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "잘못된 Token 입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 토큰입니다."),

    //HISTORY
    HISTORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재 하지 않는 히스토리 입니다."),

    //CHALLENGE
    CHALLENGE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재 하지 않는 챌린지 입니다."),
    CHALLENGE_UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "챌린지를 삭제할 권한이 없습니다."),
    CHALLENGE_ALREADY_RESERVED_ERROR(HttpStatus.BAD_REQUEST, "이미 예약한 챌린지입니다."),

    //DATE
    DATE_INVALID_ERROR(HttpStatus.BAD_REQUEST, "날짜가 유효하지 않습니다."),

    //CATEGORY
    CATEGORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 입니다."),

    //PARTICIPANT
    PARTICIPANT_LIMIT_ERROR(HttpStatus.BAD_REQUEST, "최대 참가자 수에 도달했습니다."),

    //USERPROFILE
    USER_PROFILE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 유저 프로필 입니다."),
    POINT_NOT_ENOUGH_ERROR(HttpStatus.BAD_REQUEST, "보유 포인트가 부족합니다."),
    POINT_NOT_NEGETIVE_ERROR(HttpStatus.BAD_REQUEST, "포인트는 음수가 될 수 없습니다."),

    //IMAGE
    IMAGE_FILE_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "이미지파일은 필수 입력입니다."),
    IMAGE_FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST,"이미지 파일 업로드 에러발생."),
    IMAGE_FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST,"이미지 파일 삭제 에러발생."),
    S3_NETWORK_ERROR(HttpStatus.BAD_REQUEST,"S3 연결 에러 발생");

    private final HttpStatus status;
    private final String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
