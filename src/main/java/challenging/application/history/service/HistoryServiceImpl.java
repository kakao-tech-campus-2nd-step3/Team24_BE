package challenging.application.history.service;

import challenging.application.challenge.service.ChallengeService;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.dto.response.HistoryResponse;
import challenging.application.exception.challenge.HistoryNotFoundException;
import challenging.application.history.domain.History;
import challenging.application.history.repository.HistoryRepository;
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
        .orElseThrow(HistoryNotFoundException::new);

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
