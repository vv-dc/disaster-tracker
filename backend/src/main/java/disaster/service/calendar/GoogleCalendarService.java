package disaster.service.calendar;

import disaster.dao.calendar.CalendarEventDao;
import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.calendar.error.CalendarInvalidBounds;
import disaster.model.common.TimeSearchBounds;
import disaster.module.calendar.GoogleCalendarApiClient;
import disaster.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class GoogleCalendarService {

    private final GoogleCalendarApiClient apiClient;
    private final CalendarEventDao calendarEventDao;

    public Flux<CalendarEvent> getEventsByBoundsWithLocation(CalendarSearchDto searchDto) {
        if (!isValidSearchBounds(searchDto.getTimeBounds())) {
            var exception = new CalendarInvalidBounds();
            return Flux.error(exception);
        }
        return apiClient
            .getEventsByBounds(searchDto)
            .filter(this::isWithLocation);
    }

    public Mono<Void> saveEventsToDatabase(String calendarId, List<CalendarEvent> events) {
        return calendarEventDao.upsertCalendarEvents(calendarId, events);
    }

    public Flux<CalendarEvent> getEventsFromDatabase(CalendarSearchDto searchDto) {
        return calendarEventDao.getCalendarEventByBounds(
            searchDto.getCalendarId(),
            searchDto.getTimeBounds()
        );
    }

    private boolean isWithLocation(CalendarEvent event) {
        String location = event.getLocation();
        return location != null && !location.trim().equals("");
    }

    private boolean isValidSearchBounds(TimeSearchBounds bounds) {
        return DateTimeUtils.isAfterNow(bounds.getTimeMin())
            && DateTimeUtils.isBoundsValid(bounds.getTimeMin(), bounds.getTimeMax());
    }
}
