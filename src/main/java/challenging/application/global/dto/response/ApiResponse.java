package challenging.application.global.dto.response;

import challenging.application.global.exception.ErrorResult;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    public static ApiResponse<?> validationErrorResponse(MethodArgumentNotValidException e){
        Map<String, String> errorResult = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorResult.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ApiResponse<>("fail", 400, "유효성 검사에 통과하지 못했습니다.", errorResult);
    }

    public static <T> ApiResponse<?> errorResponse(ErrorResult error){
        return new ApiResponse<>("error", Integer.parseInt(error.getCode()), error.getMessage(), null);
    }

}
