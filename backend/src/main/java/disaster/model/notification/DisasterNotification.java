package disaster.model.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.disaster.DisasterEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DisasterNotification {
    private String integrity;
    private CalendarEvent calendarEvent;
    private List<DisasterEvent> disasterEvents;
}
