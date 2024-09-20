package challenging.application.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String nickName;

    private String role;

    protected Member() {}

    public Member(String username, String nickName, String email, String role) {
        this.username = username;
        this.nickName = nickName;
        this.email = email;
        this.role = role;
    }
}
