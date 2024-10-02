package challenging.application.domain;


import challenging.application.auth.domain.Member;
import jakarta.persistence.*;
import java.time.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Getter
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "category_id")
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

  private String imageUrl;

  private int minParticipantNum;

  private int maxParticipantNum;

  private int currentParticipantNum;


  public Challenge(Category category, Member host, String name, String body, int point,
      LocalDate date, LocalTime startTime, LocalTime endTime, String imageUrl,
      int minParticipantNum,
      int maxParticipantNum, int currentParticipantNum) {
    this.category = category;
    this.host = host;
    this.name = name;
    this.body = body;
    this.point = point;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.imageUrl = imageUrl;
    this.minParticipantNum = minParticipantNum;
    this.maxParticipantNum = maxParticipantNum;
    this.currentParticipantNum = currentParticipantNum;
  }
}
