package challenging.application.domain;


import challenging.application.challenge.domain.Challenge;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  private String description;

  @OneToMany(mappedBy = "category")
  private List<Challenge> challenges = new ArrayList<>();

  protected Category() {
  }
}
