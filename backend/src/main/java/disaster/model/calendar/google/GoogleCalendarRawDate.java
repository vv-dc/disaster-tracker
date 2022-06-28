package disaster.model.calendar.google;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GoogleCalendarRawDate {
    private ZonedDateTime dateTime;
    private String timeZone;
}
