package challenging.application.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  private String description;
}
