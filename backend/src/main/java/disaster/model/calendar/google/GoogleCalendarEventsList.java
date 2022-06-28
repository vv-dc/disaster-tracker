package disaster.model.calendar.google;

import lombok.Data;

import java.util.List;

@Data
public class GoogleCalendarEventsList {

    private String nextPageToken;
    private String nextSyncToken;
    private List<GoogleCalendarRawEvent> items;
}
