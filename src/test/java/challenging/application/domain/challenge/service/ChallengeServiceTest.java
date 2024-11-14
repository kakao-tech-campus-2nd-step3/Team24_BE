package challenging.application.domain.challenge.service;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.repository.ChallengeRepository;
import challenging.application.domain.history.repository.HistoryRepository;
import challenging.application.domain.participant.entity.Participant;
import challenging.application.domain.participant.repository.ParticipantRepository;
import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.global.dto.request.ChallengeRequest;
import challenging.application.global.dto.request.ChallengeVoteRequest;
import challenging.application.global.dto.response.chalenge.ChallengeCreateResponse;
import challenging.application.global.dto.response.chalenge.ChallengeDeleteResponse;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.chalenge.ChallengeReservationResponse;
import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.challenge.*;
import challenging.application.global.error.participant.ParticipantLimitExceededException;
import challenging.application.global.error.user.UnauthorizedException;
import challenging.application.global.error.user.UserNotFoundException;
import challenging.application.global.images.ImageService;
import java.lang.reflect.Field;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.*;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ChallengeService challengeService;

    private Challenge ongoingChallenge;
    private Challenge waitChallenge;
    private Member member1;
    private Member member2;
    private Participant participant1;
    private Participant participant2;
    private ChallengeRequest challengeRequest;
    private MultipartFile multipartFile;

    @Test
    @DisplayName("챌린지 ID로 조회 - 성공")
    void getChallengeByIdAndDate_Success() {
        //given
        given(challengeRepository.findById(1L)).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.countByChallengeId(1L)).willReturn(2);

        //when
        ChallengeGetResponse response = challengeService.getChallengeById(1L);

        //then
        assertAll(
                () -> assertThat(response.challengeId()).isEqualTo(ongoingChallenge.getId()),
                () -> assertThat(response.challengeName()).isEqualTo(ongoingChallenge.getName()),
                () -> assertThat(response.challengeBody()).isEqualTo(ongoingChallenge.getBody()),
                () -> assertThat(response.point()).isEqualTo(ongoingChallenge.getPoint()),
                () -> assertThat(response.categoryId()).isEqualTo(ongoingChallenge.getCategory().getCategoryCode()),
                () -> assertThat(response.challengeDate()).isEqualTo(ongoingChallenge.getDate().toString()),
                () -> assertThat(response.startTime()).isEqualTo(ongoingChallenge.getStartTime().toString()),
                () -> assertThat(response.endTime()).isEqualTo(ongoingChallenge.getEndTime().toString()),
                () -> assertThat(response.imageUrl()).isEqualTo(ongoingChallenge.getImgUrl()),
                () -> assertThat(response.minParticipantNum()).isEqualTo(ongoingChallenge.getMinParticipantNum()),
                () -> assertThat(response.maxParticipantNum()).isEqualTo(ongoingChallenge.getMaxParticipantNum()),
                () -> assertThat(response.currentParticipantNum()).isEqualTo(2),
                () -> assertThat(response.hostUuid()).isEqualTo(ongoingChallenge.getHost().getUuid())
        );
    }

    @Test
    @DisplayName("존재하지 않는 챌린지 ID로 조회 - 실패")
    void getChallengeByIdAndDate_Failure() {
        // given
        given(challengeRepository.findById(anyLong())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> challengeService.getChallengeById(1L))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessage(ErrorCode.CHALLENGE_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("전체 챌린지 조회 - 챌린지 존재하는 경우")
    void getChallengesByCategoryAndDate_Success() {
        // given
        List<Challenge> challenges = List.of(ongoingChallenge);
        given(challengeRepository.findDateTimeAfter(any(LocalDate.class), any(LocalTime.class))).willReturn(challenges);

        // when
        List<ChallengeGetResponse> response = challengeService.getChallengesByDate();

        // then
        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(response.get(0).challengeId()).isEqualTo(ongoingChallenge.getId()),
                () -> assertThat(response.get(0).challengeName()).isEqualTo(ongoingChallenge.getName())
        );
    }

    @Test
    @DisplayName("전체 챌린지 조회 - 챌린지가 존재하지 않는 경우")
    void getChallengesByCategoryAndDate_EXCEPTION_INVALID_DATE() {
        // given
        given(challengeRepository.findDateTimeAfter(any(LocalDate.class), any(LocalTime.class))).willReturn(
                Collections.emptyList());

        // when
        List<ChallengeGetResponse> responses = challengeService.getChallengesByDate();

        // then
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("대기 중인 챌린지 조회")
    void findWaitingChallenges() {
        // given
        Participant ongoingParticipant = new Participant(ongoingChallenge, member1);
        Participant endedParticipant = new Participant(waitChallenge, member1);

        // when
        when(participantRepository.findAllByMemberId(member1.getId())).thenReturn(
                List.of(ongoingParticipant, endedParticipant));
        when(participantRepository.countByChallengeId(anyLong())).thenReturn(3);

        List<ChallengeGetResponse> waitingChallenges = challengeService.findWaitingChallenges(member1);

        // then
        assertEquals(1, waitingChallenges.size());
        assertEquals(waitChallenge.getId(), waitingChallenges.get(0).challengeId());
    }

    @Test
    @DisplayName("챌린지 생성 - 성공")
    void createChallenge_Success() {
        // given
        when(memberRepository.findByUuid(member1.getUuid())).thenReturn(Optional.of(member1));
        when(challengeRepository.save(any(Challenge.class))).thenReturn(ongoingChallenge);

        String expectedImgUrl = "http://mock-s3-url.com/challenge-image.jpg";
        when(imageService.imageloadChallenge(multipartFile, ongoingChallenge.getId())).thenReturn(expectedImgUrl);

        // when
        ChallengeCreateResponse response = challengeService.createChallenge(challengeRequest, multipartFile);

        // then
        assertNotNull(response);
    }


    @Test
    @DisplayName("챌린지 생성 - 사용자 찾을 수 없음")
    void createChallenge_UserNotFoundException() {
        // given
        given(memberRepository.findByUuid(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.createChallenge(challengeRequest, multipartFile))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("챌린지 생성 - 이미지 업로드 실패 예외")
    void createChallenge_ImageUploadException() {
        // given
        when(memberRepository.findByUuid(member1.getUuid())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.createChallenge(challengeRequest, multipartFile))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("챌린지 삭제 성공")
    void deleteChallenge_Success() {
        // given
        given(challengeRepository.findById(1L)).willReturn(Optional.of(ongoingChallenge));

        // when
        ChallengeDeleteResponse response = challengeService.deleteChallenge(1L, member1);

        // then
        assertThat(response.challengeId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("챌린지 삭제 실패 - 존재하지 않는 챌린지")
    void deleteChallengeThrowsChallengeNotFoundException() {
        // given
        given(challengeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.deleteChallenge(1L, member1))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessage(ErrorCode.CHALLENGE_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("챌린지 삭제 실패 - 권한 없는 사용자")
    void deleteChallengeThrowsUnauthorizedException() {
        // given
        given(challengeRepository.findById(1L)).willReturn(Optional.of(ongoingChallenge));

        // when & then
        assertThatThrownBy(() -> challengeService.deleteChallenge(1L, member2))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.UNAUTHORIZED_USER_ERROR.getMessage());
    }


    @Test
    @DisplayName("챌린지 예약 - 성공")
    void reserveChallengeSuccess() {
        // given
        given(challengeRepository.findById(ongoingChallenge.getId())).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.findParticipantByChallengeIdAndMemberId(ongoingChallenge.getId(), member1.getId()))
                .willReturn(Optional.empty());
        given(participantRepository.countByChallengeId(ongoingChallenge.getId())).willReturn(2);

        // when
        ChallengeReservationResponse response = challengeService.reserveChallenge(ongoingChallenge.getId(), member1);

        // then
        assertEquals(ongoingChallenge.getId(), response.challengeId());
        assertEquals(member1.getUuid(), response.uuid());
    }

    @Test
    @DisplayName("챌린지 예약 실패 - 이미 예약된 챌린지")
    void reserveChallengeThrowsAlreadyReservedException() {
        // given
        given(challengeRepository.findById(ongoingChallenge.getId())).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.findParticipantByChallengeIdAndMemberId(ongoingChallenge.getId(), member1.getId()))
                .willReturn(Optional.of(new Participant(ongoingChallenge, member1)));

        // when & then
        assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member1))
                .isInstanceOf(AlreadyReservedException.class)
                .hasMessage(ErrorCode.CHALLENGE_ALREADY_RESERVED_ERROR.getMessage());
    }

    @Test
    @DisplayName("챌린지 예약 실패 - 참가 인원 초과")
    void reserveChallengeThrowsParticipantLimitExceededException() {
        // given
        given(challengeRepository.findById(1L)).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.countByChallengeId(1L)).willReturn(4);

        // when & then
        assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member1))
                .isInstanceOf(ParticipantLimitExceededException.class)
                .hasMessage(ErrorCode.PARTICIPANT_LIMIT_ERROR.getMessage());
    }

    @Test
    @DisplayName("챌린지 예약 실패 - 존재하지 않는 챌린지")
    void reserveChallengeThrowsChallengeNotFoundException() {
        // given
        given(challengeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member1))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessage(ErrorCode.CHALLENGE_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    @DisplayName("챌린지 찾기 - 존재할 때")
    void findOnChallengeSuccess() {
        // given
        given(challengeRepository.findById(ongoingChallenge.getId())).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.countByChallengeId(ongoingChallenge.getId())).willReturn(3);

        // when
        ChallengeGetResponse response = challengeService.findOneChallenge(ongoingChallenge.getId());

        // then
        assertEquals(ongoingChallenge.getId(), response.challengeId());
        assertEquals("운동 하자", response.challengeName());
        assertEquals(3, response.currentParticipantNum());
    }

    @Test
    @DisplayName("챌린지 찾기 - 존재X")
    void findOneChallenge_ShouldThrowExceptionWhenChallengeNotFound() {
        // given
        given(challengeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.findOneChallenge(1L))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessage(ErrorCode.CHALLENGE_NOT_FOUND_ERROR.getMessage()
                );
    }

    @Test
    @DisplayName("챌린지 참여 취소 - 참가자 삭제 및 포인트 반환")
    void cancelChallenge_ShouldCancelParticipationAndReturnPoints() {
        // given
        given(challengeRepository.findById(ongoingChallenge.getId())).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.findParticipantByChallengeIdAndMemberId(ongoingChallenge.getId(), member1.getId()))
                .willReturn(Optional.of(participant1));

        // when
        challengeService.cancelChallenge(ongoingChallenge.getId(), member1);

        // then
        verify(participantRepository, times(1)).delete(participant1);
        verify(member1.getUserProfile(), times(1)).addPoint(ongoingChallenge.getPoint());
    }

    @Test
    @DisplayName("챌린지 참여 취소 - 존재하지 않는 챌린지")
    void cancelChallenge_ShouldThrowExceptionWhenChallengeNotFound() {
        // given
        given(challengeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.cancelChallenge(1L, member1))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessage(ErrorCode.CHALLENGE_NOT_FOUND_ERROR.getMessage()
                );
    }

    @Test
    @DisplayName("챌린지 참여 취소 - 참여자 찾지 못한 경우")
    void cancelChallenge_ShouldThrowExceptionWhenParticipantNotFound() {
        // given
        given(challengeRepository.findById(ongoingChallenge.getId())).willReturn(Optional.of(ongoingChallenge));
        given(participantRepository.findParticipantByChallengeIdAndMemberId(ongoingChallenge.getId(), member1.getId()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.cancelChallenge(ongoingChallenge.getId(), member1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("투표 수행 성공 - 포인트 분배")
    void voteChallenge_Success() {
        // given
        ChallengeVoteRequest request = new ChallengeVoteRequest(member1.getUuid());
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(ongoingChallenge));
        when(participantRepository.findAllByChallengeId(1L)).thenReturn(List.of(participant1, participant2));

        int successPoint = member2.getUserProfile().getPoint() + 2000;
        int banPoint = member1.getUserProfile().getPoint();

        // when
        challengeService.voteChallenge(1L, request);

        // then
        assertThat(member2.getUserProfile().getPoint()).isEqualTo(successPoint);
        assertThat(member1.getUserProfile().getPoint()).isEqualTo(banPoint);
    }

    @Test
    @DisplayName("투표 수행 - 챌린지 찾을 수 없을 때")
    void voteChallenge_ShouldThrowExceptionWhenChallengeNotFound() {
        // given
        given(challengeRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> challengeService.voteChallenge(1L, new ChallengeVoteRequest(participant1.getMember().getUuid())))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessage(ErrorCode.CHALLENGE_NOT_FOUND_ERROR.getMessage());
    }


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        UserProfile userProfile1 = mock(UserProfile.class);
        UserProfile userProfile2 = new UserProfile(null, "User2", 2000);

        member1 = new Member("pnu", "pnu", "pnu@pusan.ac.kr", userProfile1);
        member2 = new Member("other", "other", "other@pusan.ac.kr", userProfile2);

        Field member1IdField = Member.class.getDeclaredField("id");
        member1IdField.setAccessible(true);
        member1IdField.set(member1, 1L);

        Field member2IdField = Member.class.getDeclaredField("id");
        member2IdField.setAccessible(true);
        member2IdField.set(member2, 2L);

        ongoingChallenge = Challenge.builder()
                .body("운동 챌린지")
                .category(Category.SPORT)
                .date(LocalDate.now())
                .point(1000)
                .name("운동 하자")
                .startTime(LocalTime.now().minusHours(1))
                .endTime(LocalTime.now().plusHours(1))
                .host(member1)
                .minParticipantNum(2)
                .maxParticipantNum(4)
                .build();

        waitChallenge = Challenge.builder()
                .body("운동 챌린지")
                .category(Category.SPORT)
                .date(LocalDate.now())
                .point(1000)
                .name("운동 하자")
                .startTime(LocalTime.now().plusHours(2))
                .endTime(LocalTime.now().plusHours(4))
                .host(member1)
                .minParticipantNum(2)
                .maxParticipantNum(4)
                .build();

        Field challenge1IdField = Challenge.class.getDeclaredField("id");
        challenge1IdField.setAccessible(true);
        challenge1IdField.set(ongoingChallenge, 1L);

        Field challenge2IdField = Challenge.class.getDeclaredField("id");
        challenge2IdField.setAccessible(true);
        challenge2IdField.set(waitChallenge, 2L);

        participant1 = new Participant(ongoingChallenge, member1);
        participant2 = new Participant(ongoingChallenge, member2);

        challengeRequest = new ChallengeRequest(
                member1.getUuid(),
                "운동 챌린지",
                "운동을 하자",
                1,
                1000,
                "2024-10-10",
                "10:00",
                "12:00",
                2,
                4,
                "test.com"
        );

        multipartFile = mock(MultipartFile.class);
    }

}

