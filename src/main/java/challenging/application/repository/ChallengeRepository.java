package challenging.application.repository;

import challenging.application.domain.Challenge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
  List<Challenge> findByCategoryIdAndDate(int categoryId, String date);
}
