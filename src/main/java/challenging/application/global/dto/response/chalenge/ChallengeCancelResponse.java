package challenging.application.global.dto.response.chalenge;

public record ChallengeCancelResponse(
        Long challenegeId,

        String uuid
) {
    public static ChallengeCancelResponse of(Long challenegeId, String uuid){
        return new ChallengeCancelResponse(challenegeId, uuid);
    }
}
