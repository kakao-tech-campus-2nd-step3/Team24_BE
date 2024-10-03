package challenging.application.exception;

import challenging.application.exception.challenge.CategoryNotFoundException;
import challenging.application.exception.challenge.ChallengeNotFoundException;
import challenging.application.exception.challenge.InvalidDateException;
import challenging.application.exception.challenge.UserNotFoundException;
import challenging.application.util.ResponseUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseUtil {

  @ExceptionHandler(ChallengeNotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(ChallengeNotFoundException ex) {
    return createResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex) {
    return errorMessage(ex.getMessage());
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    return createResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
