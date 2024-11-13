package challenging.application.domain.challenge.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.repository.ChallengeRepository;
import challenging.application.domain.history.entity.History;
import challenging.application.domain.history.repository.HistoryRepository;
import challenging.application.domain.participant.entity.Participant;
import challenging.application.domain.participant.repository.ParticipantRepository;
import challenging.application.global.dto.request.ChallengeVoteRequest;
import challenging.application.global.dto.response.chalenge.ChallengeCreateResponse;
import challenging.application.global.dto.response.chalenge.ChallengeDeleteResponse;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.chalenge.ChallengeReservationResponse;
import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.challenge.AlreadyReservedException;
import challenging.application.global.error.challenge.ChallengeNotFoundException;
import challenging.application.global.error.participant.ParticipantLimitExceededException;
import challenging.application.global.error.user.UnauthorizedException;
import challenging.application.global.error.user.UserNotFoundException;
import challenging.application.global.dto.request.ChallengeRequest;

import challenging.application.global.images.ImageService;
import java.time.*;
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
  private final ImageService imageService;
  private final HistoryRepository historyRepository;

  public ChallengeService(ChallengeRepository challengeRepository,
      MemberRepository memberRepository,
      ParticipantRepository participantRepository, ImageService imageService,
      HistoryRepository historyRepository) {
    this.challengeRepository = challengeRepository;
    this.memberRepository = memberRepository;
    this.participantRepository = participantRepository;
    this.imageService = imageService;
    this.historyRepository = historyRepository;
  }

  // 챌린지 단건 조회
  @Transactional(readOnly = true)
  public ChallengeGetResponse getChallengeById(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    return ChallengeGetResponse.fromEntity(challenge, currentParticipantNum);
  }

  // 전체 챌린지 조회
  @Transactional(readOnly = true)
  public List<ChallengeGetResponse> getChallengesByDate() {
    LocalDateTime current = LocalDateTime.now();


    List<Challenge> challenges = challengeRepository.findDateTimeAfter(
        current.toLocalDate(),
        current.toLocalTime()
    );

    return challenges.stream()
        .map(
             challenge -> {
                int currentParticipantNum = participantRepository.countByChallengeId(challenge.getId());

                return ChallengeGetResponse.fromEntity(challenge, currentParticipantNum);
            })
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ChallengeGetResponse> findWaitingChallenges(Member member) {
    return participantRepository.findAllByMemberId(member.getId()).stream()
            .map(Participant::getChallenge)
            .filter(challenge -> !challenge.isEndChallenge())
            .map(challenge -> {
              int currentParticipantNum = participantRepository.countByChallengeId(challenge.getId());
              return ChallengeGetResponse.fromEntity(challenge, currentParticipantNum);
            })
            .collect(Collectors.toList());
  }


  // 챌린지 생성
  @Transactional
  public ChallengeCreateResponse createChallenge(
      ChallengeRequest challengeRequestDTO,
      MultipartFile multipartFile) {

    Member host = memberRepository.findByUuid(challengeRequestDTO.hostUuid())
            .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND_ERROR));

    Challenge challenge = Challenge.builder()
        .host(host)
        .name(challengeRequestDTO.challengeName())
        .body(challengeRequestDTO.challengeBody())
        .point(challengeRequestDTO.point())
        .category(Category.findByCategoryCode(challengeRequestDTO.categoryId()))
        .date(LocalDate.parse(challengeRequestDTO.challengeDate()))
        .startTime(LocalTime.parse(challengeRequestDTO.startTime()))
        .endTime(LocalTime.parse(challengeRequestDTO.endTime()))
        .imgUrl(null)
        .minParticipantNum(challengeRequestDTO.minParticipantNum())
        .maxParticipantNum(challengeRequestDTO.maxParticipantNum())
        .build();

    Challenge savedChallenge = challengeRepository.save(challenge);

    String imgUrl = null;

    imgUrl = imageService.imageload(multipartFile, savedChallenge.getId());

    challenge.updateImgUrl(imgUrl);

    host.getUserProfile().usePoint(challengeRequestDTO.point());

    Participant participant = new Participant(savedChallenge, host);

    participantRepository.save(participant);

    return new ChallengeCreateResponse(savedChallenge.getId(),savedChallenge.getImgUrl());
  }


  // 챌린지 삭제
  @Transactional
  public ChallengeDeleteResponse deleteChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    if (!challenge.getHost().getUuid().equals(user.getUuid())) {
      throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER_ERROR);
    }

    List<Participant> participants = challenge.getParticipants();
    for (Participant participant : participants) {
      Member member = participant.getMember();
      member.getUserProfile().addPoint(challenge.getPoint());
    }

    imageService.deleteImageByUrl(challenge.getImgUrl());

    challengeRepository.deleteById(challenge.getId());

    return new ChallengeDeleteResponse(challengeId);
  }

  // 챌린지 예약
  @Transactional
  public ChallengeReservationResponse reserveChallenge(Long challengeId, Member user) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    if (participantRepository.findParticipantByChallengeIdAndMemberId(challengeId, user.getId()).isPresent()) {
      throw new AlreadyReservedException(ErrorCode.CHALLENGE_ALREADY_RESERVED_ERROR);
    }

    int currentParticipantNum = participantRepository.countByChallengeId(challengeId);

    if (currentParticipantNum >= challenge.getMaxParticipantNum()) {
      throw new ParticipantLimitExceededException(ErrorCode.PARTICIPANT_LIMIT_ERROR);
    }

    user.getUserProfile().usePoint(challenge.getPoint());

    Participant participant = new Participant(challenge, user);
    participantRepository.save(participant);

    return new ChallengeReservationResponse(challengeId,user.getUuid());
  }

  @Transactional(readOnly = true)
  public ChallengeGetResponse findOneChallenge(Long challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    int participantNum = participantRepository.countByChallengeId(challengeId);

    return ChallengeGetResponse.fromEntity(challenge, participantNum);
  }

  @Transactional
  public void cancelChallenge(Long challengeId, Member member){
    Challenge challenge = challengeRepository.findById(challengeId)
            .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    Participant participant = participantRepository.findParticipantByChallengeIdAndMemberId(challengeId, member.getId())
            .orElseThrow(() -> new RuntimeException());

    participantRepository.delete(participant);

    member.getUserProfile().addPoint(challenge.getPoint());
  }

  @Transactional
  public void voteChallenge(Long challengeId, ChallengeVoteRequest challengeVoteRequest) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeNotFoundException(ErrorCode.CHALLENGE_NOT_FOUND_ERROR));

    List<Participant> participants = participantRepository.findAllByChallengeId(challengeId);

    long successCount = participants.stream()
        .filter(participant -> !participant.getMember().getUuid().equals(challengeVoteRequest.banUuid()))
        .count();

    int dividedPoints = (challenge.getPoint() * participants.size()) / (int) successCount ;

    for (Participant participant : participants) {
      Member member = participant.getMember();

      boolean isSucceed = !member.getUuid().equals(challengeVoteRequest.banUuid());

      int point = 0;

      if (isSucceed){
        point = dividedPoints;
      }

      if (isSucceed) {
        member.getUserProfile().addPoint(point);
        memberRepository.save(member);
      }

      History history = History.builder()
          .isSucceed(isSucceed)
          .isHost(member.equals(challenge.getHost()))
          .point(point)
          .member(member)
          .challenge(challenge)
          .build();

      historyRepository.save(history);
    }
  }
}
