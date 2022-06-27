package disaster.model.calendar.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CalendarGenericHttpError extends RuntimeException {

    private int code;
    private String status;
    private String message;
}
