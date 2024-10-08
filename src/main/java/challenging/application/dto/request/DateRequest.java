package challenging.application.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public record DateRequest(

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "날짜와 시간은 필수 입력 값입니다.")
        String date
) {
}
