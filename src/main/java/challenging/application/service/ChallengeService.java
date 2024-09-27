package challenging.application.service;


import challenging.application.domain.Challenge;
import challenging.application.dto.response.ChallengeResponseDTO;
import challenging.application.exeption.challenge.*;
import challenging.application.repository.ChallengeRepository;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

  private ChallengeRepository challengeRepository;

  public ChallengeResponseDTO getChallengeByIdAndDate(Long challengeId, String date) {
    if (date == null || date.isEmpty()) {
      throw new InvalidDateException("날짜가 유효하지 않습니다.");
    }

    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException("존재 하지 않는 챌린지 입니다."));

    return ChallengeResponseDTO.fromEntity(challenge);
  }
}

