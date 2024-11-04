package challenging.application.global.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorValidationResult {
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    public ErrorValidationResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
