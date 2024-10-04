package challenging.application.dto.request;

public record ChallengeRequestDTO(
    Long hostId,
    int categoryId,
    String challengeName,
    String challengeBody,
    int point,
    String challengeDate,
    String startTime,
    String endTime,
    String imageUrl,
    int minParticipantNum,
    int maxParticipantNum

) {




}
