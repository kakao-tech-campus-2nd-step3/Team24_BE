package challenging.application.repository;

import challenging.application.model.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // 이메일을 userId로 사용하는 메소드
    Optional<UserProfile> findByUserId(String userId);
}
