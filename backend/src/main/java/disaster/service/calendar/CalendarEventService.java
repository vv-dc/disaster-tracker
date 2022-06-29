package disaster.service.calendar;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.notification.NotificationUpdateReason;
import reactor.core.publisher.Flux;


public interface CalendarEventService {
    Flux<CalendarEvent> getCalendarEventsByUpdateReason(NotificationUpdateReason reason, CalendarSearchDto searchDto);

    Flux<NotificationUpdateReason> getCalendarUpdateNotifier();
}
