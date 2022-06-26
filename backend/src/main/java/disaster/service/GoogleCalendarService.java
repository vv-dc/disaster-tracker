package disaster.service;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.module.calendar.GoogleCalendarApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    private boolean isWithLocation(CalendarEvent event) {
        String location = event.getLocation();
        return location != null && !location.trim().equals("");
    }
}
