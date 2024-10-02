package challenging.application.service;

import challenging.application.auth.repository.MemberRepository;
import challenging.application.domain.Challenge;
import challenging.application.dto.request.ChallengeRequestDTO;
import challenging.application.dto.response.ChallengeResponseDTO;
import challenging.application.exception.challenge.*;
import challenging.application.repository.*;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final CategoryRepository categoryRepository;

  public ChallengeService(ChallengeRepository challengeRepository,
      MemberRepository memberRepository, CategoryRepository categoryRepository) {
    this.challengeRepository = challengeRepository;
    this.memberRepository = memberRepository;
    this.categoryRepository = categoryRepository;
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

  // 챌린지 생성
  public Long createChallenge(ChallengeRequestDTO challengeRequestDTO) {
    var host = memberRepository.findById(challengeRequestDTO.hostId())
        .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

    var category = categoryRepository.findById(challengeRequestDTO.categoryId())
        .orElseThrow(() -> new CategoryNotFoundException("해당 카테고리를 찾을 수 없습니다."));

    Challenge challenge = new Challenge(
        category,
        host,
        challengeRequestDTO.challengeName(),
        challengeRequestDTO.challengeBody(),
        challengeRequestDTO.point(),
        LocalDate.parse(challengeRequestDTO.challengeDate()),
        LocalTime.parse(challengeRequestDTO.startTime()),
        LocalTime.parse(challengeRequestDTO.endTime()),
        challengeRequestDTO.imageUrl(),
        challengeRequestDTO.minParticipantNum(),
        challengeRequestDTO.maxParticipantNum(),
        0
    );

    Challenge savedChallenge = challengeRepository.save(challenge);
    return savedChallenge.getId();
  }


  // 챌린지 삭제
  public void deleteChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException("존재 하지 않는 챌린지 입니다."));

    challengeRepository.delete(challenge);
  }


}

