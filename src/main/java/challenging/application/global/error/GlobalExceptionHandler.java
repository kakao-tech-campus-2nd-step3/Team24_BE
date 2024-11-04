package challenging.application.global.error;

import challenging.application.global.error.response.ErrorResult;
import challenging.application.global.error.response.ErrorValidationResult;
import challenging.application.global.error.challenge.AlreadyReservedException;
import challenging.application.global.error.category.CategoryNotFoundException;
import challenging.application.global.error.challenge.ChallengeNotFoundException;
import challenging.application.global.error.history.HistoryNotFoundException;
import challenging.application.global.error.date.InvalidDateException;
import challenging.application.global.error.participant.ParticipantLimitExceededException;
import challenging.application.global.error.user.UnauthorizedException;
import challenging.application.global.error.user.UserNotFoundException;
import challenging.application.global.dto.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> invalidRequestHandler(MethodArgumentNotValidException e) {
    ErrorValidationResult errorValidationResult = new ErrorValidationResult();

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      errorValidationResult.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(ApiResponse.validationErrorResponse(errorValidationResult));
  }

  @ExceptionHandler(ChallengeNotFoundException.class)
  public ResponseEntity<ApiResponse<?>> handleChallengeNotFoundException(ChallengeNotFoundException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(HistoryNotFoundException.class)
  public ResponseEntity<ApiResponse<?>> historyNotFoundException(HistoryNotFoundException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity<ApiResponse<?>> handleInvalidDateException(InvalidDateException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ApiResponse<?>> handleCategoryNotFoundException(CategoryNotFoundException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(AlreadyReservedException.class)
  public ResponseEntity<ApiResponse<?>> handleAlreadyReservedException(AlreadyReservedException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }

  @ExceptionHandler(ParticipantLimitExceededException.class)
  public ResponseEntity<ApiResponse<?>> handleAlreadyReservedException(ParticipantLimitExceededException e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }
}
