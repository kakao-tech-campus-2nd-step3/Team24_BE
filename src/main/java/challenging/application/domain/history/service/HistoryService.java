package challenging.application.domain.history.service;

import challenging.application.global.dto.response.history.HistoryGetResponse;

import java.util.List;

public interface HistoryService {

    HistoryGetResponse findOneHistory(Long memberId, Long historyId);

    List<HistoryGetResponse> findAllHistory(Long memberId);

}
