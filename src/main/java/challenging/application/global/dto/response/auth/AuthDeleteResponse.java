package challenging.application.global.dto.response.auth;

import challenging.application.domain.auth.entity.Member;

public record AuthDeleteResponse(
        String uuid
) {
    public static AuthDeleteResponse from(String uuid){
        return new AuthDeleteResponse(uuid);
    }
}
