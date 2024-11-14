package challenging.application.global.error;

import challenging.application.global.error.response.ErrorResult;
import challenging.application.global.error.response.ErrorValidationResult;
import challenging.application.global.dto.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

  @ResponseBody
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ApiResponse<?>> imageExceptionHandler(MissingServletRequestPartException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.imageErrorResponse());
  }

  @ExceptionHandler(Team24Exception.class)
  public ResponseEntity<ApiResponse<?>> team24ExceptionHandler(Team24Exception e) {
    ErrorResult errorResult = new ErrorResult(e.getStatusCode(), e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.errorResponse(errorResult));
  }
}
