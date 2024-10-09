package challenging.application.challenge;

import challenging.application.challenge.domain.Challenge;
import challenging.application.challenge.repository.ChallengeRepository;
import challenging.application.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ChallengeRepositoryTest {

  @Autowired
  private ChallengeRepository challengeRepository;

  @BeforeEach
  void setUp() {
    Challenge challenge1 = Challenge.builder()
        .category(Category.SPORTS)
        .name("아침 조깅")
        .body("매일 아침 조깅 챌린지에 참여하세요.")
        .point(100)
        .date(LocalDate.of(2024, 11, 11))
        .startTime(LocalTime.of(6, 0))
        .endTime(LocalTime.of(7, 0))
        .imageUrl("https://i.namu.wiki/i/h1OsaRJTYVMZqNJgV18rsMgnuW93lXxkWb1ujRDctn0egswSk1VXIEeZVBZ9zRad-PK9S1YhMTyFWTKK2lyEJieoSiOHBERaR4ilxP-7zjbsZvVC-muvIA50Of-aCAnJBj6NGrRv7j4uwXQfCDlneA.webp")
        .minParticipantNum(5)
        .maxParticipantNum(20)
        .build();

    Challenge challenge2 = Challenge.builder()
        .category(Category.SPORTS)
        .name("저녁 조깅")
        .body("매일 저녁 조깅 챌린지에 참여하세요.")
        .point(100)
        .date(LocalDate.of(2024, 11, 11))
        .startTime(LocalTime.of(18, 0))
        .endTime(LocalTime.of(19, 0))
        .imageUrl("https://health.chosun.com/site/data/img_dir/2022/03/30/2022033000861_0.jpg")
        .minParticipantNum(5)
        .maxParticipantNum(20)
        .build();

    challengeRepository.save(challenge1);
    challengeRepository.save(challenge2);
  }

  @Test
  @DisplayName("카테고리와 날짜로 챌린지를 조회 - 정상적으로 조회")
  void findByCategoryAndDateTest() {
    List<Challenge> foundChallenges = challengeRepository.findByCategoryAndDate(Category.SPORTS, LocalDate.of(2024, 11, 11));

    assertThat(foundChallenges).isNotEmpty();
    assertThat(foundChallenges.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("잘못된 카테고리로 조회 - 빈 리스트 반환")
  void findByInvalidCategoryTest() {
    List<Challenge> foundChallenges = challengeRepository.findByCategoryAndDate(Category.HOBBY, LocalDate.of(2024, 11, 11));

    assertThat(foundChallenges).isEmpty();
  }

  @Test
  @DisplayName("잘못된 날짜로 조회 - 빈 리스트 반환")
  void findByInvalidDateTest() {
    List<Challenge> foundChallenges = challengeRepository.findByCategoryAndDate(Category.SPORTS, LocalDate.of(2025, 1, 1));

    assertThat(foundChallenges).isEmpty();
  }
}
