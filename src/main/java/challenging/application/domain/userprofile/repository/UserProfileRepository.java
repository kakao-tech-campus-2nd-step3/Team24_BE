package challenging.application.domain.userprofile.repository;

import challenging.application.domain.userprofile.domain.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByMemberId(Long memberId);

    @Query("SELECT u FROM UserProfile u WHERE u.member.uuid = :uuid")
    Optional<UserProfile> findByMemberUuid(@Param("uuid") String uuid);
}
