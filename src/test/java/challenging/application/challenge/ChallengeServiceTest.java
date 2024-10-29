package challenging.application.challenge;

import challenging.application.auth.domain.Member;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.challenge.domain.Category;
import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.repository.ChallengeRepository;
import challenging.application.challenge.service.ChallengeService;
import challenging.application.domain.Participant;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.exception.challenge.*;
import challenging.application.repository.ParticipantRepository;
import java.lang.reflect.Field;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.*;
import java.util.*;

import static challenging.application.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

  @InjectMocks
  private ChallengeService challengeService;

  private Challenge challenge;
  private Member member1;
  private Member member2;
  private ChallengeRequest challengeRequestDTO;

  @BeforeEach
  void setUp() throws Exception {
    member1 = new Member("pnu", "pnu", "pnu@pusan.ac.kr", "ROLE_USER");
    member2 = new Member("other", "other", "other@pusan.ac.kr", "ROLE_USER");

    Field member1IdField = Member.class.getDeclaredField("id");
    member1IdField.setAccessible(true);
    member1IdField.set(member1, 1L);

    Field member2IdField = Member.class.getDeclaredField("id");
    member2IdField.setAccessible(true);
    member2IdField.set(member2, 2L);

    challengeRequestDTO = new ChallengeRequest(
        1L,
        1,
        "운동 챌린지",
        "운동을 하자",
        1000,
        "2024-10-10",
        "10:00",
        "12:00",
        "abc.png",
        2,
        4
    );

    challenge = Challenge.builder()
        .body("운동 챌린지")
        .category(Category.SPORT)
        .date(LocalDate.now())
        .point(1000)
        .name("운동 하자")
        .startTime(LocalTime.now())
        .endTime(LocalTime.now())
        .host(member1)
        .imageUrl("abc.png")
        .minParticipantNum(2)
        .maxParticipantNum(4)
        .build();

    Field idField = Challenge.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(challenge, 1L);
  }


  @Test
  @DisplayName("챌린지 ID로 조회 - 성공")
  void getChallengeByIdAndDate_Success() {
    //given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));
    given(participantRepository.countByChallengeId(1L)).willReturn(2L);

    //when
    ChallengeResponse response = challengeService.getChallengeById(1L);

    //then
    assertThat(response.challengeName()).isEqualTo(challenge.getName());
    assertThat(response.currentParticipantNum()).isEqualTo(2);
  }

  @Test
  @DisplayName("존재하지 않는 챌린지 ID로 조회 - 실패")
  void getChallengeByIdAndDate_Failure() {
    //given
    given(challengeRepository.findById(1L)).willReturn(Optional.empty());

    //expected
    assertThatThrownBy(() -> challengeService.getChallengeById(1L))
        .isInstanceOf(ChallengeNotFoundException.class)
        .hasMessage(CHALLENGE_NOT_FOUND);
  }

  @Test
  @DisplayName("카테고리별 챌린지 조회 - 성공")
  void getChallengesByCategoryAndDate_Success() {
    // given
    String date = "2024-11-11:00:00";
    Category category = Category.SPORT;
    List<Challenge> challenges = new ArrayList<>();

    challenges.add(challenge);
    given(challengeRepository.findByCategoryAndDateTimeAfter(
            category,
            LocalDate.parse("2024-11-11"),
            LocalTime.of(0, 0)))
        .willReturn(challenges);
    given(participantRepository.countByChallengeId(challenge.getId())).willReturn(2L);

    // when
    List<ChallengeResponse> response =
            challengeService.getChallengesByCategoryAndDate(category.getCategoryCode(), date);

    // then
    assertThat(response.size()).isEqualTo(1);
    assertThat(response.get(0).challengeName()).isEqualTo(challenge.getName());
  }

  @Test
  @DisplayName("카테고리별 챌린지 조회 실패 - 유효하지 않은 날짜")
  void getChallengesByCategoryAndDate_EXCEPTION_INVALID_DATE() {
    //given
    String invalidDate = "이상한 날짜";
    int categoryId = 1;

    //expected
    assertThatThrownBy(() -> challengeService.getChallengesByCategoryAndDate(categoryId, invalidDate))
        .isInstanceOf(InvalidDateException.class)
        .hasMessage(INVALID_DATE);
  }


  @Test
  @DisplayName("카테고리별 챌린지 조회 실패 - 카테고리 없음")
  void getChallengesByCategoryAndDate_EXCEPTION_NoCategory() {
    //given
    int invalidCategoryId = 999;
    String date = "2024-11-11:00:00";

    //expected
    assertThatThrownBy(() -> challengeService.getChallengesByCategoryAndDate(invalidCategoryId, date))
        .isInstanceOf(CategoryNotFoundException.class)
        .hasMessage(CATEGORY_NOT_FOUND);
  }


  @Test
  @DisplayName("챌린지 생성 성공")
  void createChallenge_Success() {
    //given
    ChallengeRequest request = new ChallengeRequest(
        member1.getId(),
        Category.SPORT.getCategoryCode(),
        "수영하자",
        "초보도 같이",
        200,
        "2024-11-11",
        "08:00",
        "09:00",
        "abc.png",
        2,
        4
    );

    given(memberRepository.findById(member1.getId())).willReturn(Optional.of(member1));
    given(challengeRepository.save(any(Challenge.class))).willReturn(challenge);

    //when
    Long savedChallengeId = challengeService.createChallenge(request);

    //then
    assertThat(savedChallengeId).isEqualTo(challenge.getId());
    verify(participantRepository).save(any());
  }

  @Test
  @DisplayName("챌린지 생성 실패 - 사용자 찾을 수 없음")
  void createChallenge_EXCEPTION_UserNotFound() {
    //given
    given(memberRepository.findById(challengeRequestDTO.hostId())).willReturn(Optional.empty());

    //expected
    assertThatThrownBy(() -> challengeService.createChallenge(challengeRequestDTO))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(USER_NOT_FOUND);

    verify(challengeRepository, never()).save(any(Challenge.class));
    verify(participantRepository, never()).save(any(Participant.class));
  }

  @Test
  @DisplayName("챌린지 삭제 성공")
  void deleteChallenge_Success() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));

    // when
    challengeService.deleteChallenge(1L, member1);

    // then
    verify(challengeRepository).delete(challenge);
  }

  @Test
  @DisplayName("챌린지 삭제 실패 - 존재하지 않는 챌린지")
  void deleteChallengeThrowsChallengeNotFoundException() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.empty());

    // expected
    assertThatThrownBy(() -> challengeService.deleteChallenge(1L, member1))
        .isInstanceOf(ChallengeNotFoundException.class);
  }

  @Test
  @DisplayName("챌린지 삭제 실패 - 권한 없는 사용자")
  void deleteChallengeThrowsUnauthorizedException() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));

    // expected
    assertThatThrownBy(() -> challengeService.deleteChallenge(1L, member2))
        .isInstanceOf(UnauthorizedException.class)
        .hasMessage(UNAUTHORIZED_EXCEPTION);
  }


  @Test
  @DisplayName("챌린지 예약 - 성공")
  void reserveChallengeSuccess() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));
    given(participantRepository.existsByChallengeIdAndMemberId(1L, 1L)).willReturn(false);
    given(participantRepository.countByChallengeId(1L)).willReturn(2L);

    ArgumentCaptor<Participant> participantCaptor = ArgumentCaptor.forClass(Participant.class);

    // when
    challengeService.reserveChallenge(1L, member1);

    // then
    verify(participantRepository).save(participantCaptor.capture());

    Participant capturedParticipant = participantCaptor.getValue();
    assertEquals(challenge, capturedParticipant.getChallenge());
    assertEquals(member1, capturedParticipant.getMember());
  }

  @Test
  @DisplayName("챌린지 예약 실패 - 이미 예약된 챌린지")
  void reserveChallengeThrowsAlreadyReservedException() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));
    given(participantRepository.existsByChallengeIdAndMemberId(1L, 1L)).willReturn(
        true);

    // expected
    assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member1))
        .isInstanceOf(AlreadyReservedException.class)
        .hasMessage(ALREADY_RESERVED_EXCEPTION);
  }

  @Test
  @DisplayName("챌린지 예약 실패 - 참가 인원 초과")
  void reserveChallengeThrowsParticipantLimitExceededException() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));
    given(participantRepository.existsByChallengeIdAndMemberId(1L, 1L)).willReturn(false);
    given(participantRepository.countByChallengeId(1L)).willReturn(4L);

    // expected
    assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member1))
        .isInstanceOf(ParticipantLimitExceededException.class)
        .hasMessage(PARTICIPANT_LIMIT_EXCEEDED);
  }

  @Test
  @DisplayName("챌린지 예약 실패 - 존재하지 않는 챌린지")
  void reserveChallengeThrowsChallengeNotFoundException() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.empty());

    // expected
    assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member1))
        .isInstanceOf(ChallengeNotFoundException.class)
        .hasMessage(CHALLENGE_NOT_FOUND);
  }
}
