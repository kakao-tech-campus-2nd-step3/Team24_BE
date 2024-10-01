package challenging.application.service;


import challenging.application.domain.Challenge;
import challenging.application.dto.response.ChallengeResponseDTO;
import challenging.application.exception.challenge.CategoryNotFoundException;
import challenging.application.exception.challenge.ChallengeNotFoundException;
import challenging.application.exception.challenge.InvalidDateException;
import challenging.application.repository.ChallengeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;

  public ChallengeService(ChallengeRepository challengeRepository) {
    this.challengeRepository = challengeRepository;
  }

  // 챌린지 단건 조회
  public ChallengeResponseDTO getChallengeByIdAndDate(Long challengeId, String date) {
    if (date == null || date.isEmpty()) {
      throw new InvalidDateException("날짜가 유효하지 않습니다.");
    }

    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException("존재 하지 않는 챌린지 입니다."));

    return ChallengeResponseDTO.fromEntity(challenge);
  }


  // 카테고리별 챌린지 조회
  public List<ChallengeResponseDTO> getChallengesByCategoryAndDate(int categoryId, String date) {
    List<Challenge> challenges = challengeRepository.findByCategoryIdAndDate(categoryId, date);

    if (challenges.isEmpty()) {
      throw new CategoryNotFoundException("옳지 않은 카테고리 ID 입니다.");
    }

    return challenges.stream()
        .map(ChallengeResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }


}

