package challenging.application.challenge.service;

import challenging.application.auth.domain.Member;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.challenge.domain.Category;
import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.repository.ChallengeRepository;
import challenging.application.domain.*;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.response.ChallengeCreateResponse;
import challenging.application.dto.response.ChallengeDeleteResponse;
import challenging.application.dto.response.ChallengeReservationResponse;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.exception.challenge.*;
import challenging.application.repository.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  // 챌린지 단건 조회
  @Transactional(readOnly = true)
  public ChallengeResponse getChallengeById(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    return ChallengeResponse.fromEntity(challenge, currentParticipantNum);
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

  // 카테고리별 챌린지 조회
  @Transactional(readOnly = true)
  public List<ChallengeResponse> getChallengesByCategoryAndDate(int categoryId, String date) {
    LocalDateTime localDateTime = parseDate(date);

    Category category = Category.findByCategoryCode(categoryId);

    List<Challenge> challenges = challengeRepository.findByCategoryAndDateTimeAfter(
        category,
        localDateTime.toLocalDate(),
        localDateTime.toLocalTime()
    );

    return challenges.stream()
        .map(
             challenge -> {
                int currentParticipantNum = participantRepository.countByChallengeId(challenge.getId());
                return ChallengeResponse.fromEntity(challenge, currentParticipantNum);
            })
        .collect(Collectors.toList());
  }

  // 챌린지 생성
  @Transactional
  public ChallengeCreateResponse createChallenge(ChallengeRequest challengeRequestDTO) {
    Member host = memberRepository.findById(challengeRequestDTO.hostId())
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
        .imageExtension(challengeRequestDTO.imageExtension())
        .minParticipantNum(challengeRequestDTO.minParticipantNum())
        .maxParticipantNum(challengeRequestDTO.maxParticipantNum())
        .build();

    Challenge savedChallenge = challengeRepository.save(challenge);

    Participant participant = new Participant(savedChallenge, host);
    participantRepository.save(participant);

    return new ChallengeCreateResponse(savedChallenge.getId());
  }


  // 챌린지 삭제
  @Transactional
  public ChallengeDeleteResponse deleteChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    if (!challenge.getHost().getId().equals(user.getId())) {
      throw new UnauthorizedException();
    }

    challengeRepository.delete(challenge);

    return new ChallengeDeleteResponse(challengeId);
  }

  // 챌린지 예약
  @Transactional
  public ChallengeReservationResponse reserveChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    if (participantRepository.existsByChallengeIdAndMemberId(challengeId, user.getId())) {
      throw new AlreadyReservedException();
    }

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    if (currentParticipantNum >= challenge.getMaxParticipantNum()) {
      throw new ParticipantLimitExceededException();
    }

    Participant participant = new Participant(challenge, user);
    participantRepository.save(participant);

    return new ChallengeReservationResponse(challengeId,user.getId());
  }

  @Transactional(readOnly = true)
  public ChallengeResponse findOneChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    int participantNum = participantRepository.countByChallengeId(challengeId).intValue();

    return ChallengeResponse.fromEntity(challenge, participantNum);
  }


}
