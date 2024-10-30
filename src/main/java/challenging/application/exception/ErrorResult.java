package challenging.application.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResult {
    private final String code;
    private final String message;

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
