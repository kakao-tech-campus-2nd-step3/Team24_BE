package challenging.application.global.dto.response.chalenge;

import challenging.application.domain.challenge.entity.Challenge;

public record ChallengeGetResponse(
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

  public static ChallengeGetResponse fromEntity(Challenge challenge, int currentParticipantNum, String imgUrl) {
    return new ChallengeGetResponse(
        challenge.getId(),
        challenge.getName(),
        challenge.getBody(),
        challenge.getPoint(),
        challenge.getDate().toString(),
        challenge.getStartTime().toString(),
        challenge.getEndTime().toString(),
        imgUrl,
        challenge.getMinParticipantNum(),
        challenge.getMaxParticipantNum(),
        currentParticipantNum,
        challenge.getHost().getId(),
        challenge.getCategory().getCategoryCode()
    );
  }
}
