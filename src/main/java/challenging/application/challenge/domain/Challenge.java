package challenging.application.challenge.domain;

import challenging.application.auth.domain.Member;
import jakarta.persistence.*;
import java.time.*;
import lombok.*;

@Entity
@Getter
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Category category;

  @ManyToOne
  @JoinColumn(name = "host_id")
  private Member host;

  private String name;

  private String body;

  private int point;

  private LocalDate date;

  private LocalTime startTime;

  private LocalTime endTime;

  private String imageExtension;

  private int minParticipantNum;

  private int maxParticipantNum;

  protected Challenge() {
  }

  @Builder
  public Challenge(Category category,
      Member host,
      String name,
      String body,
      int point,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      String imageExtension,
      int minParticipantNum,
      int maxParticipantNum) {
    this.category = category;
    this.host = host;
    this.name = name;
    this.body = body;
    this.point = point;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.imageExtension = imageExtension;
    this.minParticipantNum = minParticipantNum;
    this.maxParticipantNum = maxParticipantNum;
  }
}
