package disaster.model.calendar.google;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class GoogleCalendarRawDate {
    private Date date;
    private ZonedDateTime dateTime;
    private String timeZone;
}
