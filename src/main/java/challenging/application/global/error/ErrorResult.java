package challenging.application.global.error;

import lombok.Getter;

@Getter
public class ErrorResult {
    private final String code;
    private final String message;

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
