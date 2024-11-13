package challenging.application.domain.history.service;

import challenging.application.domain.challenge.service.ChallengeService;
import challenging.application.domain.history.repository.HistoryRepository;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.history.HistoryGetResponse;
import challenging.application.global.error.ErrorCode;
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
public class HistoryService {

  private final HistoryRepository historyRepository;
  private final ChallengeService challengeService;

  public HistoryGetResponse findOneHistory(Long memberId, Long historyId) {
    History history = historyRepository.findHistoryByMemberIdAndId(memberId, historyId)
        .orElseThrow(() -> new HistoryNotFoundException(ErrorCode.HISTORY_NOT_FOUND_ERROR));

    ChallengeGetResponse challengeGetResponseDTO = challengeService.findOneChallenge(history.getChallenge().getId());

    return HistoryGetResponse.of(challengeGetResponseDTO, history);
  }

  public List<HistoryGetResponse> findAllHistory(Long memberId) {
    List<History> histories = historyRepository.findAllByMemberId(memberId);

    List<HistoryGetResponse> historyGetRespons = histories.stream()
        .map(history -> {
          ChallengeGetResponse challengeGetResponseDTO = challengeService.findOneChallenge(history.getChallenge().getId());
          return HistoryGetResponse.of(challengeGetResponseDTO, history);
        })
        .collect(Collectors.toList());

    return historyGetRespons;
  }
}
