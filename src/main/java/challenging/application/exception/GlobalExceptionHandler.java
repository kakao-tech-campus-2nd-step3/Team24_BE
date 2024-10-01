package challenging.application.exception;

import challenging.application.exception.challenge.ChallengeNotFoundException;
import challenging.application.exception.challenge.InvalidDateException;
import challenging.application.util.ResponseUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseUtil{

  // NotFound 관련 예외 이곳에 넣기
  @ExceptionHandler(ChallengeNotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(ChallengeNotFoundException ex){
    return createResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex) {
    return errorMessage(ex.getMessage());
  }


}
