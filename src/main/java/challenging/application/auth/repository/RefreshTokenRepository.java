package challenging.application.auth.repository;


import challenging.application.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByToken(String Token);

    Optional<RefreshToken> findByToken(String refreshToken);

    @Transactional
    void deleteByToken(String Token);
}