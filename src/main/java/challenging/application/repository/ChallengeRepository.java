package challenging.application.repository;

import challenging.application.domain.Challenge;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
  List<Challenge> findByCategoryIdAndDate(int category_id, LocalDate date);
}
