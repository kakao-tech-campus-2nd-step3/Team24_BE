package challenging.application.history.domain;

import challenging.application.auth.domain.Member;
import challenging.application.challenge.domain.Challenge;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isSucceed;

    private Boolean isHost;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    protected History() {
    }

    @Builder
    public History(Boolean isSucceed, Boolean isHost, String imageUrl, Member member, Challenge challenge) {
        this.isSucceed = isSucceed;
        this.isHost = isHost;
        this.imageUrl = imageUrl;
        this.member = member;
        this.challenge = challenge;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Boolean getSucceed() {
        return isSucceed;
    }

    public Boolean getHost() {
        return isHost;
    }
}
