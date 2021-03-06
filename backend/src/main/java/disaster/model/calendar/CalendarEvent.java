package disaster.model.calendar;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CalendarEvent {

    private String id;
    private String eventType;
    private String status;
    private String htmlLink;
    private String summary;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
}
