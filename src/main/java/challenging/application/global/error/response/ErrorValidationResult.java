package challenging.application.global.error.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorValidationResult {
    public static int ERROR_STATUS_CODE = 400;
    public static String ERROR_MESSAGE = "유효성 검사를 통과하지 못했습니다.";
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}