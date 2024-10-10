package challenging.application.challenge.service;

import challenging.application.auth.domain.Member;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.challenge.domain.Category;
import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.repository.ChallengeRepository;
import challenging.application.domain.*;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.exception.challenge.*;
import challenging.application.repository.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final ParticipantRepository participantRepository;
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd:HH:mm");

  public ChallengeService(ChallengeRepository challengeRepository,
      MemberRepository memberRepository,
      ParticipantRepository participantRepository) {
    this.challengeRepository = challengeRepository;
    this.memberRepository = memberRepository;
    this.participantRepository = participantRepository;
  }

  private LocalDateTime parseDate(String date) {
    if (date == null || date.trim().isEmpty()) {
      throw new InvalidDateException();
    }

    try {
      return LocalDateTime.parse(date, dateTimeFormatter);
    } catch (DateTimeParseException e) {
      throw new InvalidDateException();
    }
  }


  // 챌린지 단건 조회
  public ChallengeResponse getChallengeByIdAndDate(Long challengeId, String date) {
    LocalDateTime parsedDate = parseDate(date);

    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId).intValue();

    return ChallengeResponse.fromEntity(challenge, currentParticipantNum);
  }

  // 카테고리별 챌린지 조회
  public List<ChallengeResponse> getChallengesByCategoryAndDate(int categoryId, String date) {
    LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);

    List<Challenge> challenges = challengeRepository.findByCategoryIdAndStartDateTimeAfter(
        categoryId, localDateTime);

    if (challenges.isEmpty()) {
      throw new CategoryNotFoundException();
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
        .orElseThrow(UserNotFoundException::new);

    Category category = Category.findByCategoryCode(challengeRequestDTO.categoryId());

    Challenge challenge = Challenge.builder()
        .category(category)
        .host(host)
        .name(challengeRequestDTO.challengeName())
        .body(challengeRequestDTO.challengeBody())
        .point(challengeRequestDTO.point())
        .date(LocalDate.parse(challengeRequestDTO.challengeDate()))
        .startTime(LocalTime.parse(challengeRequestDTO.startTime()))
        .endTime(LocalTime.parse(challengeRequestDTO.endTime()))
        .imageUrl(challengeRequestDTO.imageUrl())
        .minParticipantNum(challengeRequestDTO.minParticipantNum())
        .maxParticipantNum(challengeRequestDTO.maxParticipantNum())
        .build();

    Challenge savedChallenge = challengeRepository.save(challenge);

    Participant participant = new Participant(savedChallenge, host);
    participantRepository.save(participant);
    return savedChallenge.getId();
  }


  // 챌린지 삭제
  public void deleteChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    if (!challenge.getHost().getId().equals(user.getId())) {
      throw new UnauthorizedException();
    }

    challengeRepository.delete(challenge);
  }

  // 챌린지 예약
  public void reserveChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    if (participantRepository.existsByChallengeIdAndMemberId(challengeId, user.getId())) {
      throw new AlreadyReservedException();
    }

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId).intValue();

    if (currentParticipantNum >= challenge.getMaxParticipantNum()) {
      throw new ParticipantLimitExceededException();
    }

    Participant participant = new Participant(challenge, user);
    participantRepository.save(participant);
  }

  public ChallengeResponse findOneChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    int participantNum = participantRepository.countByChallengeId(challengeId).intValue();

    return ChallengeResponse.fromEntity(challenge, participantNum);
  }


}