package challenging.application.domain.challenge.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.repository.ChallengeRepository;
import challenging.application.domain.participant.entity.Participant;
import challenging.application.domain.participant.repository.ParticipantRepository;
import challenging.application.global.error.challenge.AlreadyReservedException;
import challenging.application.global.error.challenge.ChallengeNotFoundException;
import challenging.application.global.error.challenge.InvalidDateException;
import challenging.application.global.error.challenge.ParticipantLimitExceededException;
import challenging.application.global.error.challenge.UnauthorizedException;
import challenging.application.global.error.challenge.UserNotFoundException;
import challenging.application.global.dto.request.ChallengeRequest;
import challenging.application.global.dto.response.ChallengeCreateResponse;
import challenging.application.global.dto.response.ChallengeDeleteResponse;
import challenging.application.global.dto.response.ChallengeReservationResponse;
import challenging.application.global.dto.response.ChallengeResponse;
import challenging.application.images.S3PresignedImageService;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final ParticipantRepository participantRepository;
  private final S3PresignedImageService s3PresignedImageService;
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd:HH:mm");

  public ChallengeService(ChallengeRepository challengeRepository,
      MemberRepository memberRepository,
      ParticipantRepository participantRepository,
      S3PresignedImageService s3PresignedImageService) {
    this.challengeRepository = challengeRepository;
    this.memberRepository = memberRepository;
    this.participantRepository = participantRepository;
    this.s3PresignedImageService = s3PresignedImageService;
  }

  // 챌린지 단건 조회
  @Transactional(readOnly = true)
  public ChallengeResponse getChallengeById(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(ChallengeNotFoundException::new);

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    String challengePresignedGetUrl = null;

    if (challenge.getImageExtension() != null){
      challengePresignedGetUrl = s3PresignedImageService.createChallengePresignedGetUrl(
          challenge.getImageExtension(),
          challengeId
      );
    }

    return ChallengeResponse.fromEntity(challenge, currentParticipantNum,challengePresignedGetUrl);
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
               String challengePresignedGetUrl = null;

               if (challenge.getImageExtension() != null) {
                 challengePresignedGetUrl = s3PresignedImageService.createChallengePresignedGetUrl(
                     challenge.getImageExtension(),
                     challenge.getId()
                 );
               }
                return ChallengeResponse.fromEntity(challenge, currentParticipantNum,challengePresignedGetUrl);
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

    String challengePresignedPutUrl = null;

    if (challenge.getImageExtension() != null) {
      challengePresignedPutUrl = s3PresignedImageService.createChallengePresignedPutUrl(
          challenge.getImageExtension(),
          challenge.getId()
      );
    }

    return new ChallengeCreateResponse(savedChallenge.getId(),challengePresignedPutUrl);
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

    int participantNum = participantRepository.countByChallengeId(challengeId);

    String challengePresignedGetUrl = null;

    if (challenge.getImageExtension() != null) {
      challengePresignedGetUrl = s3PresignedImageService.createChallengePresignedGetUrl(
          challenge.getImageExtension(),
          challenge.getId()
      );
    }

    return ChallengeResponse.fromEntity(challenge, participantNum,challengePresignedGetUrl);
  }


}
