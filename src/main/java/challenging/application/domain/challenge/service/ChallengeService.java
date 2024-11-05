package challenging.application.domain.challenge.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.repository.ChallengeRepository;
import challenging.application.domain.participant.entity.Participant;
import challenging.application.domain.participant.repository.ParticipantRepository;
import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.challenge.AlreadyReservedException;
import challenging.application.global.error.challenge.ChallengeNotFoundException;
import challenging.application.global.error.date.InvalidDateException;
import challenging.application.global.error.participant.ParticipantLimitExceededException;
import challenging.application.global.error.user.UnauthorizedException;
import challenging.application.global.error.user.UserNotFoundException;
import challenging.application.global.dto.request.ChallengeRequest;
import challenging.application.global.dto.response.ChallengeCreateResponse;
import challenging.application.global.dto.response.ChallengeDeleteResponse;
import challenging.application.global.dto.response.ChallengeReservationResponse;
import challenging.application.global.dto.response.ChallengeResponse;
import challenging.application.global.images.ImageService;
import challenging.application.global.images.S3PresignedImageService;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private final ParticipantRepository participantRepository;
  private final S3PresignedImageService s3PresignedImageService;
  private final ImageService imageService;
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd:HH:mm");

  public ChallengeService(ChallengeRepository challengeRepository,
      MemberRepository memberRepository,
      ParticipantRepository participantRepository, S3PresignedImageService s3PresignedImageService,
      ImageService imageService) {
    this.challengeRepository = challengeRepository;
    this.memberRepository = memberRepository;
    this.participantRepository = participantRepository;
    this.s3PresignedImageService = s3PresignedImageService;
    this.imageService = imageService;
  }

  // 챌린지 단건 조회
  @Transactional(readOnly = true)
  public ChallengeResponse getChallengeById(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    return ChallengeResponse.fromEntity(challenge, currentParticipantNum);
  }

  // 전체 챌린지 조회
  @Transactional(readOnly = true)
  public List<ChallengeResponse> getChallengesByCategoryAndDate() {
    LocalDateTime current = LocalDateTime.now();


    List<Challenge> challenges = challengeRepository.findDateTimeAfter(
        current.toLocalDate(),
        current.toLocalTime()
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
  public ChallengeCreateResponse createChallenge(
      ChallengeRequest challengeRequestDTO,
      MultipartFile multipartFile) {
    Member host = memberRepository.findById(challengeRequestDTO.hostId())
            .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND_ERROR));

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
        .imgUrl(null)
        .minParticipantNum(challengeRequestDTO.minParticipantNum())
        .maxParticipantNum(challengeRequestDTO.maxParticipantNum())
        .build();

    Long challengId = challenge.getId();

    String imgUrl = null;

    imgUrl = imageService.imageload(multipartFile, challengId);

    challenge.updateImgUrl(imgUrl);

    Challenge savedChallenge = challengeRepository.save(challenge);

    Participant participant = new Participant(savedChallenge, host);
    participantRepository.save(participant);


    return new ChallengeCreateResponse(savedChallenge.getId(),savedChallenge.getImgUrl());
  }


  // 챌린지 삭제
  @Transactional
  public ChallengeDeleteResponse deleteChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    if (!challenge.getHost().getId().equals(user.getId())) {
      throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER_ERROR);
    }

    imageService.deleteImageByUrl(challenge.getImgUrl());

    challengeRepository.delete(challenge);

    return new ChallengeDeleteResponse(challengeId);
  }

  // 챌린지 예약
  @Transactional
  public ChallengeReservationResponse reserveChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    if (participantRepository.existsByChallengeIdAndMemberId(challengeId, user.getId())) {
      throw new AlreadyReservedException(ErrorCode.CHALLENGE_ALREADY_RESERVED_ERROR);
    }

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    if (currentParticipantNum >= challenge.getMaxParticipantNum()) {
      throw new ParticipantLimitExceededException(ErrorCode.PARTICIPANT_LIMIT_ERROR);
    }

    Participant participant = new Participant(challenge, user);
    participantRepository.save(participant);

    return new ChallengeReservationResponse(challengeId,user.getId());
  }

  @Transactional(readOnly = true)
  public ChallengeResponse findOneChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    int participantNum = participantRepository.countByChallengeId(challengeId);

    return ChallengeResponse.fromEntity(challenge, participantNum);
  }


}
