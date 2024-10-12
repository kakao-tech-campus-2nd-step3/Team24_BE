package challenging.application.history.repository;

import challenging.application.history.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByMemberId(Long memberId);
    Optional<History> findHistoryByMemberIdAndId(Long memberId, Long id);
}
