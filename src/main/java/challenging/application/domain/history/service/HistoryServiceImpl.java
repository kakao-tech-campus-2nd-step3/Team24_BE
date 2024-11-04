package challenging.application.domain.history.service;

import challenging.application.domain.challenge.service.ChallengeService;
import challenging.application.domain.history.repository.HistoryRepository;
import challenging.application.global.dto.response.ChallengeResponse;
import challenging.application.global.dto.response.HistoryResponse;
import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.challenge.ChallengeNotFoundException;
import challenging.application.global.error.history.HistoryNotFoundException;
import challenging.application.domain.history.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryServiceImpl implements HistoryService {

  private final HistoryRepository historyRepository;
  private final ChallengeService challengeService;

  @Override
  public HistoryResponse findOneHistory(Long memberId, Long historyId) {
    History history = historyRepository.findHistoryByMemberIdAndId(memberId, historyId)
        .orElseThrow(() -> new HistoryNotFoundException(ErrorCode.HISTORY_NOT_FOUND_ERROR));

    ChallengeResponse challengeResponseDTO = challengeService.findOneChallenge(history.getChallenge().getId());

    return HistoryResponse.of(challengeResponseDTO, history);
  }

  @Override
  public List<HistoryResponse> findAllHistory(Long memberId) {
    List<History> histories = historyRepository.findAllByMemberId(memberId);

    List<HistoryResponse> historyResponses = histories.stream()
        .map(history -> {
          ChallengeResponse challengeResponseDTO = challengeService.findOneChallenge(history.getChallenge().getId());
          return HistoryResponse.of(challengeResponseDTO, history);
        })
        .collect(Collectors.toList());

    return historyResponses;
  }
}
