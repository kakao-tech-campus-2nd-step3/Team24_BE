package challenging.application.domain;

import challenging.application.auth.domain.Member;
import challenging.application.challenge.domain.Challenge;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Member member;

  protected Participant() {
  }

  public Participant(Challenge challenge, Member member) {
    this.challenge = challenge;
    this.member = member;
  }
}
