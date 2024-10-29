package challenging.application.dto.response;

import challenging.application.history.domain.History;

public record HistoryResponse(
    ChallengeResponse challenge,

    Boolean is_succeed,

    Boolean is_host,

    Integer point
) {

  public static HistoryResponse of(ChallengeResponse challenge, History history) {
    return new HistoryResponse(challenge, history.getSucceed(), history.getHost(),history.getPoint());
  }
}
