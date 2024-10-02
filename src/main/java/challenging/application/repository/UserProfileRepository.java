package challenging.application.repository;

import challenging.application.model.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Member의 id를 사용하여 UserProfile을 조회
    Optional<UserProfile> findByUserId(Long userId);
}
