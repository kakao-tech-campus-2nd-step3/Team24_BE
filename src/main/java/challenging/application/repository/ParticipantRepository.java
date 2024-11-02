package challenging.application.repository;

import challenging.application.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
  Integer countByChallengeId(Long challengeId);
  boolean existsByChallengeIdAndMemberId(Long challengeId, Long memberId);

}
