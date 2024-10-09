package challenging.application.challenge;

import challenging.application.auth.domain.Member;
import challenging.application.auth.repository.MemberRepository;
import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.repository.ChallengeRepository;
import challenging.application.challenge.service.ChallengeService;
import challenging.application.domain.Category;
import challenging.application.dto.request.ChallengeRequest;
import challenging.application.dto.response.ChallengeResponse;
import challenging.application.exception.challenge.*;
import challenging.application.repository.ParticipantRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.*;
import java.util.*;

import static challenging.application.exception.ExceptionMessage.CATEGORY_NOT_FOUND;
import static challenging.application.exception.ExceptionMessage.INVALID_DATE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
  private Member member;

  @BeforeEach
  void setUp() {
    member = new Member("테스트", "테스트 닉네임", "test@test.com", "ROLE_USER");

    challenge = Challenge.builder()
        .id(1L)
        .category(Category.SPORTS)
        .name("아침 조깅")
        .body("매일 아침 조깅 챌린지에 참여하세요.")
        .point(100)
        .date(LocalDate.of(2024, 11, 11))
        .startTime(LocalTime.of(6, 0))
        .endTime(LocalTime.of(7, 0))
        .imageUrl(
            "https://i.namu.wiki/i/h1OsaRJTYVMZqNJgV18rsMgnuW93lXxkWb1ujRDctn0egswSk1VXIEeZVBZ9zRad-PK9S1YhMTyFWTKK2lyEJieoSiOHBERaR4ilxP-7zjbsZvVC-muvIA50Of-aCAnJBj6NGrRv7j4uwXQfCDlneA.webp")
        .minParticipantNum(5)
        .maxParticipantNum(20)
        .host(member)
        .build();
  }

  @Test
  @DisplayName("챌린지 ID로 조회 - 성공")
  void getChallengeByIdAndDateTest() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));
    given(participantRepository.countByChallengeId(1L)).willReturn(5L);

    // when
    ChallengeResponse response = challengeService.getChallengeByIdAndDate(1L, "2024-11-11:06:00");

    // then
    assertThat(response.challengeName()).isEqualTo("아침 조깅");
    assertThat(response.currentParticipantNum()).isEqualTo(5);
  }

  @Test
  @DisplayName("존재하지 않는 챌린지 ID로 조회 - 실패")
  void getChallengeByIdAndDate_NotFoundTest() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.empty());

    // when / then
    assertThatThrownBy(() -> challengeService.getChallengeByIdAndDate(1L, "2024-11-11:06:00"))
        .isInstanceOf(ChallengeNotFoundException.class);
  }

  @Test
  @DisplayName("빈 날짜로 챌린지를 조회할 때 InvalidDateException 발생")
  void getChallengeByIdAndDate_EmptyDate_ThrowsInvalidDateException() {
    // given
    Long challengeId = 1L;

    // when & then
    assertThatThrownBy(() -> challengeService.getChallengeByIdAndDate(challengeId, ""))
        .isInstanceOf(InvalidDateException.class)
        .hasMessage(INVALID_DATE);
  }


  @Test
  @DisplayName("카테고리와 날짜로 챌린지를 조회 - 성공")
  void getChallengesByCategoryAndDateTest() {
    // given
    given(challengeRepository.findByCategoryAndDate(eq(Category.SPORTS), any(LocalDate.class)))
        .willReturn(List.of(challenge));

    given(participantRepository.countByChallengeId(any(Long.class))).willReturn(5L);

    // when
    List<ChallengeResponse> responses = challengeService.getChallengesByCategoryAndDate("SPORTS",
        "2024-11-11:06:00");

    // then
    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).challengeName()).isEqualTo("아침 조깅");
    assertThat(responses.get(0).currentParticipantNum()).isEqualTo(5);
  }

  @Test
  @DisplayName("존재하지 않는 카테고리로 챌린지 조회 - 예외")
  void getChallengesByCategoryAndDate_CategoryNotFoundTest() {
    // given
    given(challengeRepository.findByCategoryAndDate(eq(Category.SPORTS), any(LocalDate.class)))
        .willReturn(List.of());

    // when & then
    assertThatThrownBy(
        () -> challengeService.getChallengesByCategoryAndDate("SPORTS", "2024-11-11:06:00"))
        .isInstanceOf(CategoryNotFoundException.class)
        .hasMessage(CATEGORY_NOT_FOUND);
  }

  @Test
  @DisplayName("챌린지 생")
  void createChallengeTest() {
    // given
    given(memberRepository.findById(1L)).willReturn(Optional.of(member));
    given(challengeRepository.save(any(Challenge.class))).willReturn(challenge);

    ChallengeRequest request = new ChallengeRequest(
        1L,
        Category.SPORTS,
        "아침 조깅",
        "매일 아침 조깅 챌린지에 참여하세요.",
        100,
        "2024-11-11",
        "06:00",
        "07:00",
        "https://i.namu.wiki/i/h1OsaRJTYVMZqNJgV18rsMgnuW93lXxkWb1ujRDctn0egswSk1VXIEeZVBZ9zRad-PK9S1YhMTyFWTKK2lyEJieoSiOHBERaR4ilxP-7zjbsZvVC-muvIA50Of-aCAnJBj6NGrRv7j4uwXQfCDlneA.webp",
        5,
        20
    );

    // when
    Long challengeId = challengeService.createChallenge(request);

    // then
    assertThat(challengeId).isEqualTo(challenge.getId());
  }

  @Test
  @DisplayName("존재하지 않는 사용자가 챌린지 생성 시 - 예외")
  void createChallenge_UserNotFoundTest() {
    // given
    given(memberRepository.findById(1L)).willReturn(Optional.empty());

    ChallengeRequest request = new ChallengeRequest(
        1L,
        Category.SPORTS,
        "아침 조깅",
        "매일 아침 조깅 챌린지에 참여하세요.",
        100,
        "2024-11-11",
        "06:00",
        "07:00",
        "https://i.namu.wiki/i/h1OsaRJTYVMZqNJgV18rsMgnuW93lXxkWb1ujRDctn0egswSk1VXIEeZVBZ9zRad-PK9S1YhMTyFWTKK2lyEJieoSiOHBERaR4ilxP-7zjbsZvVC-muvIA50Of-aCAnJBj6NGrRv7j4uwXQfCDlneA.webp",
        5,
        20
    );

    // when / then
    assertThatThrownBy(() -> challengeService.createChallenge(request))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("챌린지 삭제")
  void deleteChallengeTest() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));

    // when
    challengeService.deleteChallenge(1L);

    // then
    verify(challengeRepository).delete(challenge);
  }

  @Test
  @DisplayName("존재하지 않는 챌린지를 삭제 - 예외")
  void deleteChallenge_NotFoundTest() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.empty());

    // when / then
    assertThatThrownBy(() -> challengeService.deleteChallenge(1L))
        .isInstanceOf(ChallengeNotFoundException.class);
  }

  @Test
  @DisplayName("챌린지 예약")
  void reserveChallengeTest() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.of(challenge));

    // when
    challengeService.reserveChallenge(1L, member);

    // then
    verify(participantRepository).save(any());
  }

  @Test
  @DisplayName("존재하지 않는 챌린지를 예약할 때 - 예외")
  void reserveChallenge_NotFoundTest() {
    // given
    given(challengeRepository.findById(1L)).willReturn(Optional.empty());

    // when / then
    assertThatThrownBy(() -> challengeService.reserveChallenge(1L, member))
        .isInstanceOf(ChallengeNotFoundException.class);
  }
}
