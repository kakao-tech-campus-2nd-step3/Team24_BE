package challenging.application.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String token;

    private String expiration;

    protected RefreshToken() {
    }

    public RefreshToken(String email, String token, String expiration) {
        this.email = email;
        this.token = token;
        this.expiration = expiration;
    }
}
