package challenging.application.global.dto.response;

import challenging.application.global.error.response.ErrorValidationResult;
import challenging.application.global.error.response.ErrorResult;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiResponse<T> {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String ERROR = "error";
    private final String status;
    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<?> successResponse(T data){
        return new ApiResponse<>(SUCCESS, HttpStatus.OK.value(), null, data);
    }

    public static <T> ApiResponse<?> successResponseWithMessage(String message, T data){
        return new ApiResponse<>(SUCCESS, HttpStatus.OK.value(), message, data);
    }

    public static <T> ApiResponse<?> createResponse(T data){
        return new ApiResponse<>(SUCCESS, HttpStatus.CREATED.value(), null, data);
    }

    public static ApiResponse<?> validationErrorResponse(ErrorValidationResult e){
        return new ApiResponse<>(FAIL, ErrorValidationResult.ERROR_STATUS_CODE, ErrorValidationResult.ERROR_MESSAGE, e.getValidation());
    }

    public static ApiResponse<?> errorResponse(ErrorResult error){
        return new ApiResponse<>(ERROR, error.getStatusCode(), error.getMessage(), null);
    }
}
