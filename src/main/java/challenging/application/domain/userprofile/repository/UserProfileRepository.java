package challenging.application.domain.userprofile.repository;

import challenging.application.domain.userprofile.domain.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByMemberId(Long memberId);
}
