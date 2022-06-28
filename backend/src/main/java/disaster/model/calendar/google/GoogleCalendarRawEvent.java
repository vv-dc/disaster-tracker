package disaster.model.calendar.google;

import lombok.Data;

@Data
public class GoogleCalendarRawEvent {

    private String id;
    private String eventType;
    private String status;
    private String htmlLink;
    private String summary;
    private String location;
    private GoogleCalendarRawDate start;
    private GoogleCalendarRawDate end;
}
