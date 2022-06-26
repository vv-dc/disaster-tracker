package disaster.service;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchBounds;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.calendar.error.CalendarInvalidBounds;
import disaster.module.calendar.GoogleCalendarApiClient;
import disaster.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class GoogleCalendarService {

    private final GoogleCalendarApiClient apiClient;

    @Autowired
    public GoogleCalendarService(GoogleCalendarApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Flux<CalendarEvent> getEventsByBoundsWithLocation(CalendarSearchDto searchDto) {
        return apiClient
            .getEventsByBounds(searchDto)
            .filter(this::isWithLocation);
    }

    public Flux<CalendarEvent> getEventsByBoundsStream(CalendarSearchDto searchDto) {
        if (!isValidSearchBounds(searchDto.getTimeBounds())) {
            var exception = new CalendarInvalidBounds();
            return Flux.error(exception);
        }
        return getEventsByBoundsWithLocation(searchDto)
            .repeatWhen((flux) -> Flux.interval(Duration.ofMinutes(10)));
    }

    public Flux<CalendarEvent> getEventsFromDb(String calendarId) {
        return Flux.empty();
    }

    private boolean isWithLocation(CalendarEvent event) {
        String location = event.getLocation();
        return location != null && !location.trim().equals("");
    }

    private boolean isValidSearchBounds(CalendarSearchBounds bounds) {
        return DateTimeUtils.isAfterNow(bounds.getTimeMin())
            && DateTimeUtils.isBoundsValid(bounds.getTimeMin(), bounds.getTimeMax());
    }
}
