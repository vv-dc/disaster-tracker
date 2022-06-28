package disaster.model.common;

import disaster.util.DateTimeUtils;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSearchBounds {

    private LocalDateTime timeMin; // kebab-case
    private LocalDateTime timeMax;

    public TimeSearchBounds(String timeMin, String timeMax) {
        this.timeMin = DateTimeUtils.parseToLocal(timeMin);
        this.timeMax = DateTimeUtils.parseToLocal(timeMax);
    }
}
