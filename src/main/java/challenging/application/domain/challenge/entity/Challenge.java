package challenging.application.domain.challenge.entity;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.category.Category;
import challenging.application.domain.participant.entity.Participant;
import jakarta.persistence.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "host_id")
  private Member host;

  private String name;

  private String body;

  private int point;

  @Enumerated(EnumType.STRING)
  private Category category;

  private LocalDate date;

  private LocalTime startTime;

  private LocalTime endTime;

  private String imgUrl;

  private int minParticipantNum;

  private int maxParticipantNum;

  @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Participant> participants = new ArrayList<>();

  protected Challenge() {
  }

  @Builder
  public Challenge(
      Member host,
      String name,
      String body,
      int point,
      Category category,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      String imgUrl,
      int minParticipantNum,
      int maxParticipantNum) {
    this.host = host;
    this.name = name;
    this.body = body;
    this.point = point;
    this.category = category;
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

  public boolean isEndChallenge(){
    LocalDateTime startDateTime = LocalDateTime.of(date, startTime);

    LocalDateTime currentDateTime = LocalDateTime.now();
    return !currentDateTime.isBefore(startDateTime);
  }
}
