package challenging.application.auth.repository;


import challenging.application.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByToken(String Token);

    void deleteByToken(String Token);

    Optional<RefreshToken> findByToken(String refreshToken);
}