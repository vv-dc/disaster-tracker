package disaster.model.calendar.google;

import disaster.model.calendar.CalendarEvent;
import lombok.Data;

import java.util.List;

@Data
public class GoogleCalendarEventsList {

    private String nextPageToken;
    private String nextSyncToken;
    private List<CalendarEvent> items;
}
