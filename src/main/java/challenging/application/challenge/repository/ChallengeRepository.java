package challenging.application.challenge.repository;

import challenging.application.challenge.domain.Challenge;
import challenging.application.domain.Category;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
  List<Challenge> findByCategoryAndDate(Category category, LocalDate date);
}
