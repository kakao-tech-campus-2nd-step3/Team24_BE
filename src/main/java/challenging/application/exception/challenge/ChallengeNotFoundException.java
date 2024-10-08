package challenging.application.exception.challenge;

public class ChallengeNotFoundException extends RuntimeException {

  public ChallengeNotFoundException(String message) {
    super(message);
  }

  public ChallengeNotFoundException() {
    super("존재 하지 않는 챌린지 입니다.");
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
