package challenging.application.challenge.service;

import challenging.application.auth.domain.Member;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.repository.ChallengeRepository;
import challenging.application.domain.*;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.exception.challenge.*;
import challenging.application.repository.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final CategoryRepository categoryRepository;
  private final ParticipantRepository participantRepository;
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd:HH:mm");

  public ChallengeService(ChallengeRepository challengeRepository,
      MemberRepository memberRepository, CategoryRepository categoryRepository,
      ParticipantRepository participantRepository) {
    this.challengeRepository = challengeRepository;
    this.memberRepository = memberRepository;
    this.categoryRepository = categoryRepository;
    this.participantRepository = participantRepository;
  }

  // 챌린지 단건 조회
  public ChallengeResponse getChallengeByIdAndDate(Long challengeId, String date) {
    if (date == null || date.isEmpty()) {
      throw new InvalidDateException("날짜가 유효하지 않습니다.");
    }

    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException());

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId).intValue();

    return ChallengeResponse.fromEntity(challenge, currentParticipantNum);
  }

  // 카테고리별 챌린지 조회
  public List<ChallengeResponse> getChallengesByCategoryAndDate(int categoryId, String date) {
    LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);

    List<Challenge> challenges = challengeRepository.findByCategoryIdAndDate(categoryId,  localDateTime.toLocalDate());

    if (challenges.isEmpty()) {
      throw new CategoryNotFoundException("옳지 않은 카테고리 ID 입니다.");
    }

    return challenges.stream()
        .map(challenge -> {
          int currentParticipantNum = participantRepository.countByChallengeId(challenge.getId())
              .intValue();
          return ChallengeResponse.fromEntity(challenge, currentParticipantNum);
        })
        .collect(Collectors.toList());
  }

  // 챌린지 생성
  public Long createChallenge(ChallengeRequest challengeRequestDTO) {
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
        challengeRequestDTO.maxParticipantNum());

    Challenge savedChallenge = challengeRepository.save(challenge);
    return savedChallenge.getId();
  }


  // 챌린지 삭제
  public void deleteChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException("존재 하지 않는 챌린지 입니다."));

    challengeRepository.delete(challenge);
  }

  // 챌린지 예약
  public void reserveChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException("존재 하지 않는 챌린지 입니다."));

    Participant participant = new Participant(challenge, user);
    participantRepository.save(participant);
  }


}

