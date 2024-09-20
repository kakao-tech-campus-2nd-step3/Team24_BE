package challenging.application.auth.repository;


import challenging.application.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByToken(String Token);

    void deleteByToken(String Token);
}