package challenging.application.global.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChallengeVoteRequest(
        String banUuid

) {


}
