package challenging.application.global.dto.response.chalenge;

public record ChallengeCreateResponse(
        Long challengeId,
        String imgUrl,
        String challengeUrl
)
{ }
