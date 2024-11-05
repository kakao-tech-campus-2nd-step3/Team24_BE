package challenging.application.global.dto.response;

import challenging.application.domain.challenge.entity.Challenge;

public record ChallengeResponse(
    Long challengeId,
    String challengeName,
    String challengeBody,
    int point,
    String challengeDate,
    String startTime,
    String endTime,
    String imageUrl,
    int minParticipantNum,
    int maxParticipantNum,
    int currentParticipantNum,
    Long hostId,
    int categoryId
) {

  public static ChallengeResponse fromEntity(Challenge challenge, int currentParticipantNum) {
    return new ChallengeResponse(
        challenge.getId(),
        challenge.getName(),
        challenge.getBody(),
        challenge.getPoint(),
        challenge.getDate().toString(),
        challenge.getStartTime().toString(),
        challenge.getEndTime().toString(),
        challenge.getImgUrl(),
        challenge.getMinParticipantNum(),
        challenge.getMaxParticipantNum(),
        currentParticipantNum,
        challenge.getHost().getId(),
        challenge.getCategory().getCategoryCode()
    );
  }
}
