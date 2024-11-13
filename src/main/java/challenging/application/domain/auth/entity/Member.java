package challenging.application.domain.auth.entity;

import challenging.application.domain.userprofile.domain.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String uuid;

    private String role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userprofile_id")
    private UserProfile userProfile;

    protected Member() {
    }

    public Member(String email, String username, String role, UserProfile userProfile) {
        this.email = email;
        this.username = username;
        this.role = role;
        this.userProfile = userProfile;
        this.uuid = UUID.randomUUID().toString();
    }

}
