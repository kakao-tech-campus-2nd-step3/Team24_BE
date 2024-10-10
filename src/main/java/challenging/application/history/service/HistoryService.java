package challenging.application.history.service;

import challenging.application.dto.response.HistoryResponse;

import java.util.List;

public interface HistoryService {

    HistoryResponse findOneHistory(Long memberId, Long historyId);

    List<HistoryResponse> findAllHistory(Long memberId);

}
