package challenging.application.dto.response;

import challenging.application.history.domain.History;

public record HistoryResponse(
        ChallengeResponseDTO challenge,

        Boolean is_succeed,

        Boolean is_host
) {

    public static HistoryResponse of(ChallengeResponseDTO challenge, History history){
        return new HistoryResponse(challenge, history.getSucceed(), history.getHost());
    }
}
