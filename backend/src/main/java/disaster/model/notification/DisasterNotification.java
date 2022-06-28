package disaster.model.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.disasters.HazardEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DisasterNotification {
    private CalendarEvent calendarEvent;
    private List<HazardEvent> disasterEvents;
}
