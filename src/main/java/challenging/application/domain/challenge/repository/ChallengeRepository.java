package challenging.application.domain.challenge.repository;

import challenging.application.domain.category.Category;
import challenging.application.domain.challenge.entity.Challenge;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


  @Query("SELECT c FROM Challenge c WHERE (c.date > :date OR (c.date = :date AND c.startTime > :startTime))")
  List<Challenge> findDateTimeAfter(
      @Param("date") LocalDate date,
      @Param("startTime") LocalTime startTime
  );

  @Query("SELECT c FROM Challenge c JOIN FETCH c.host WHERE c.id = :id")
  Optional<Challenge> findByIdWithHost(@Param("id") Long id);

}
