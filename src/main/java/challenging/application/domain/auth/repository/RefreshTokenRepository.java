package challenging.application.domain.auth.repository;


import challenging.application.domain.auth.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByToken(String Token);

    Optional<RefreshToken> findByToken(String refreshToken);

    Optional<RefreshToken> findRefreshTokenByMemberId(Long memberId);

    @Transactional
    void deleteByToken(String Token);
}