package disaster.model.calendar;

import lombok.Data;

@Data
public class CalendarEvent {

    private String id;
    private String eventType;
    private String status;
    private String htmlLink;
    private String summary;
    private String location;
    private CalendarEventDate start;
    private CalendarEventDate end;
}
