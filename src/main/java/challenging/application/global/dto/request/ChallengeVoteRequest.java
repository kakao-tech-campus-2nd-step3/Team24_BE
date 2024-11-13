package challenging.application.global.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChallengeVoteRequest(

    @NotNull(message = "Challeng ID는 필수 입력값입니다.")
    Long challengeId,
    @NotNull(message = "밴당한사람 UUID는 필수 입력값입니다.")
    String banUuid

) {


}
