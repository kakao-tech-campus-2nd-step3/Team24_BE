package challenging.application.challenge.repository;

import challenging.application.challenge.domain.Category;
import challenging.application.challenge.domain.Challenge;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {


  @Query("SELECT c FROM Challenge c WHERE c.category = :category AND (c.date > :date OR (c.date = :date AND c.startTime > :startTime))")
  List<Challenge> findByCategoryAndDateTimeAfter(
      @Param("category") Category category,
      @Param("date") LocalDate date,
      @Param("startTime") LocalTime startTime
  );



}