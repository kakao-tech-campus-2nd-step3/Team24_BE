package challenging.application.challenge;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import challenging.application.domain.challenge.repository.ChallengeRepository;
import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.domain.userprofile.repository.UserProfileRepository;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
class ChallengeRepositoryTest {

  @Autowired
  private ChallengeRepository challengeRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @BeforeEach
  void setUp() {
    UserProfile userProfile = new UserProfile();
    userProfileRepository.save(userProfile);

    Member host = new Member("테스트하는 사람", "테스트 닉네임", "test@example.com", userProfile);
    memberRepository.save(host);
    memberRepository.save(host);

    Challenge challenge1 = Challenge.builder()
        .host(host)
        .category(Category.SPORT)
        .name("아침 조깅")
        .body("매일 아침 조깅 챌린지에 참여하세요.")
        .point(100)
        .date(LocalDate.of(2024, 11, 11))
        .startTime(LocalTime.of(6, 0))
        .endTime(LocalTime.of(7, 0))
        .minParticipantNum(5)
        .maxParticipantNum(20)
        .build();

    Challenge challenge2 = Challenge.builder()
        .host(host)
        .category(Category.SPORT)
        .name("저녁 조깅")
        .body("매일 저녁 조깅 챌린지에 참여하세요.")
        .point(100)
        .date(LocalDate.of(2024, 11, 11))
        .startTime(LocalTime.of(18, 0))
        .endTime(LocalTime.of(19, 0))
        .minParticipantNum(5)
        .maxParticipantNum(20)
        .build();

    challengeRepository.save(challenge1);
    challengeRepository.save(challenge2);
  }

  static Stream<Arguments> dateProvider() {
    return Stream.of(
        Arguments.of(LocalDateTime.of(2024, 11, 10, 5, 0), 2), // 전날 -> 2개
        Arguments.of(LocalDateTime.of(2024, 11, 11, 5, 0), 2), // 5시 -> 2개
        Arguments.of(LocalDateTime.of(2024, 11, 11, 6, 0), 1), // 6시 -> 1개
        Arguments.of(LocalDateTime.of(2024, 11, 11, 19, 0), 0),  // 19시 -> 0개
        Arguments.of(LocalDateTime.of(2024, 11, 12, 5, 0), 0) // 다음날 -> 0개
    );
  }

  @ParameterizedTest
  @MethodSource("dateProvider")
  @DisplayName("카테고리와 날짜로 챌린지 검색")
  void testFindByCategoryAndDateAndStartTimeAfter(LocalDateTime queryDate, int expectedCount) {
    List<Challenge> challenges = challengeRepository.findByCategoryAndDateTimeAfter(
        Category.SPORT,
        queryDate.toLocalDate(),
        queryDate.toLocalTime()
    );

    assertEquals(expectedCount, challenges.size());
  }

}
