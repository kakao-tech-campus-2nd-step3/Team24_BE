package challenging.application.userprofile.repository;

import challenging.application.userprofile.domain.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Member의 id를 사용하여 UserProfile을 조회
    Optional<UserProfile> findByMemberId(Long memberId);
}
