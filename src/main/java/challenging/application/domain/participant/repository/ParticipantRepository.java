package challenging.application.domain.participant.repository;

import challenging.application.domain.participant.entity.Participant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
  Integer countByChallengeId(Long challengeId);
  boolean existsByChallengeIdAndMemberId(Long challengeId, Long memberId);
  List<Participant> findAllByChallengeId(Long challengeId);
  List<Participant> findAllByMemberId(Long memberId);

}
