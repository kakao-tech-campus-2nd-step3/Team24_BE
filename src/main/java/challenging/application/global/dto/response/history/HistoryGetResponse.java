package challenging.application.global.dto.response.history;

import challenging.application.domain.history.entity.History;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;

public record HistoryGetResponse(
    ChallengeGetResponse challenge,

    Boolean isSucceed,

    Boolean isHost,

    Integer point
) {

  public static HistoryGetResponse of(ChallengeGetResponse challenge, History history) {
    return new HistoryGetResponse(challenge, history.getSucceed(), history.getHost(),history.getPoint());
  }
}
