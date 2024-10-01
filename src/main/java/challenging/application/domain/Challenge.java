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
}
