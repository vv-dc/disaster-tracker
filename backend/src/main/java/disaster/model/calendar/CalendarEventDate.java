package disaster.model.calendar;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CalendarEventDate {

    private ZonedDateTime dateTime;
    private String timeZone;
}
