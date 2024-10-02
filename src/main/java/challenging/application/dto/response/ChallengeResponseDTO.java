package challenging.application.dto.response;
import challenging.application.domain.Challenge;

public record ChallengeResponseDTO(
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
  public static ChallengeResponseDTO fromEntity(Challenge challenge, int currentParticipantNum) {
    return new ChallengeResponseDTO(
        challenge.getName(),
        challenge.getBody(),
        challenge.getPoint(),
        challenge.getDate().toString(),
        challenge.getStartTime().toString(),
        challenge.getEndTime().toString(),
        challenge.getImageUrl(),
        challenge.getMinParticipantNum(),
        challenge.getMaxParticipantNum(),
        currentParticipantNum,
        challenge.getHost().getId(),
        challenge.getCategory().getId()
    );
  }
}
