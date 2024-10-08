package challenging.application.util;

import org.springframework.http.*;

public abstract class ResponseUtil {

  protected static <T> ResponseEntity<T> createResponse(T data, HttpStatus status) {
    return new ResponseEntity<>(data, status);
  }

  protected <T> ResponseEntity<T> successResponse(T data) {
    return createResponse(data, HttpStatus.OK);
  }

  protected ResponseEntity<String> successMessage(String message) {
    return createResponse(message, HttpStatus.OK);
  }

  protected ResponseEntity<String> errorMessage(String message) {
    return createResponse(message, HttpStatus.BAD_REQUEST);
  }

  protected <T> ResponseEntity<T> errorResponse(T data, HttpStatus status) {
    return createResponse(data, status);
  }
}
