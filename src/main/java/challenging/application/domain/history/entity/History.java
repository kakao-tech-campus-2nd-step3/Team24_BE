package challenging.application.domain.history.entity;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.challenge.entity.Challenge;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isSucceed;

    private Boolean isHost;

    private Integer point;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    protected History() {
    }

    @Builder
    public History(Boolean isSucceed, Boolean isHost, Integer point, Member member, Challenge challenge) {
        this.isSucceed = isSucceed;
        this.isHost = isHost;
        this.point = point;
        this.member = member;
        this.challenge = challenge;
    }
}
