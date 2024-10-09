package challenging.application.dto.request;

import challenging.application.domain.Category;
import jakarta.validation.constraints.*;

public record ChallengeRequest(

    @NotNull(message = "호스트 ID는 필수 입력값입니다.")
    Long hostId,
    @NotNull(message = "카테고리는 필수 입력값입니다.")
    Category category,
    @NotNull(message = "챌린지 이름은 필수 입력값입니다.")
    @Size(min = 1, message = "챌린지 이름은 비어 있을 수 없습니다.")
    String challengeName,
    @NotNull(message = "챌린지 내용은 필수 입력값입니다.")
    String challengeBody,
    @NotNull(message = "포인트는 필수 입력값입니다.")
    @Min(value = 0, message = "포인트는 0 이상이어야 합니다.")
    int point,
    @NotNull(message = "챌린지 날짜는 필수 입력값입니다.")
    String challengeDate,
    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    String startTime,
    @NotNull(message = "종료 시간은 필수 입력값입니다.")
    String endTime,
    @NotNull(message = "사진은 필수 입력 값입니다.")
    String imageUrl,
    @NotNull(message = "최소 참가자 수는 필수 입력값입니다.")
    @Min(value = 1, message = "최소 참가자 수는 1명 이상이어야 합니다.")
    int minParticipantNum,
    @NotNull(message = "최대 참가자 수는 필수 입력값입니다.")
    @Min(value = 1, message = "최대 참가자 수는 1명 이상이어야 합니다.")
    int maxParticipantNum

) {




}
