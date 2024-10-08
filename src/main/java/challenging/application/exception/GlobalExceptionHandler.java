package challenging.application.exception;

import challenging.application.exception.challenge.*;
import challenging.application.util.ResponseUtil;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseUtil{

  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResult> invalidRequestHandler(MethodArgumentNotValidException e) {
    ErrorResult errorResult = new ErrorResult("400", "잘못된 요청입니다.");

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      errorResult.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ChallengeNotFoundException.class)
  public ResponseEntity<ErrorResult> handleNotFoundException(ChallengeNotFoundException e) {
    ErrorResult errorResult = new ErrorResult("404", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HistoryNotFoundException.class)
  public ResponseEntity<ErrorResult> historyNotFoundException(HistoryNotFoundException e) {
    ErrorResult errorResult = new ErrorResult("404", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
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
