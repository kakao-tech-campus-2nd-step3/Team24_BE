package challenging.application.global.dto.response;

import challenging.application.global.error.response.ErrorValidationResult;
import challenging.application.global.error.response.ErrorResult;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final String status;
    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<?> successResponse(T data){
        return new ApiResponse<>("success", 200, null, data);
    }

    public static <T> ApiResponse<?> createResponse(T data){
        return new ApiResponse<>("success", 201, null, data);
    }

    public static ApiResponse<?> validationErrorResponse(ErrorValidationResult e){
        return new ApiResponse<>("fail", ErrorValidationResult.ERROR_STATUS_CODE, ErrorValidationResult.ERROR_MESSAGE, e.getValidation());
    }

    public static ApiResponse<?> errorResponse(ErrorResult error){
        return new ApiResponse<>("error", error.getStatusCode(), error.getMessage(), null);
    }
}
