package disaster.model.calendar;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CalendarSearchBounds {

    private ZonedDateTime timeMin; // kebab-case
    private ZonedDateTime timeMax;

    public CalendarSearchBounds(String timeMin, String timeMax) {
        this.timeMin = ZonedDateTime.parse(timeMin);
        this.timeMax = ZonedDateTime.parse(timeMax);
    }
}
