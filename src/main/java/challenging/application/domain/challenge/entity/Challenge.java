package challenging.application.domain.challenge.entity;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.category.Category;
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

  private String imgUrl;

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
      String imgUrl,
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
    this.imgUrl = imgUrl;
    this.minParticipantNum = minParticipantNum;
    this.maxParticipantNum = maxParticipantNum;
  }

  public void updateImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}
