package challenging.application.global.dto.response.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
