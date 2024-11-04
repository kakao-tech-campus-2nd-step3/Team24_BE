package challenging.application.global.exception;

import challenging.application.exception.challenge.*;
import challenging.application.global.exception.challenge.AlreadyReservedException;
import challenging.application.global.exception.challenge.CategoryNotFoundException;
import challenging.application.global.exception.challenge.ChallengeNotFoundException;
import challenging.application.global.exception.challenge.HistoryNotFoundException;
import challenging.application.global.exception.challenge.InvalidDateException;
import challenging.application.global.exception.challenge.ParticipantLimitExceededException;
import challenging.application.global.exception.challenge.UnauthorizedException;
import challenging.application.global.exception.challenge.UserNotFoundException;
import challenging.application.global.dto.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> invalidRequestHandler(MethodArgumentNotValidException e) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ApiResponse.validationErrorResponse(e));
  }

  @ExceptionHandler(ChallengeNotFoundException.class)
  public ResponseEntity<ErrorResult> handleChallengeNotFoundException(ChallengeNotFoundException e) {
    ErrorResult errorResult = new ErrorResult("404", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HistoryNotFoundException.class)
  public ResponseEntity<ErrorResult> historyNotFoundException(HistoryNotFoundException e) {
    ErrorResult errorResult = new ErrorResult("404", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity<ErrorResult> handleInvalidDateException(InvalidDateException e) {
    ErrorResult errorResult = new ErrorResult("400", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResult> handleCategoryNotFoundException(CategoryNotFoundException e) {
    ErrorResult errorResult = new ErrorResult("400", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResult> handleUserNotFoundException(UserNotFoundException e) {
    ErrorResult errorResult = new ErrorResult("404", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResult> handleUnauthorizedException(UnauthorizedException e) {
    ErrorResult errorResult = new ErrorResult("403", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AlreadyReservedException.class)
  public ResponseEntity<ErrorResult> handleAlreadyReservedException(AlreadyReservedException e) {
    ErrorResult errorResult = new ErrorResult("409", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ParticipantLimitExceededException.class)
  public ResponseEntity<ErrorResult> handleAlreadyReservedException(ParticipantLimitExceededException e) {
    ErrorResult errorResult = new ErrorResult(" ", e.getMessage());

    return new ResponseEntity<>(errorResult, HttpStatus.FORBIDDEN);
  }
}
