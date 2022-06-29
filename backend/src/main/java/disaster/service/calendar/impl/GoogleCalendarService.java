package disaster.service.calendar.impl;

import disaster.dao.calendar.CalendarEventDao;
import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.calendar.error.CalendarInvalidBounds;
import disaster.model.common.TimeSearchBounds;
import disaster.model.notification.NotificationUpdateReason;
import disaster.module.calendar.GoogleCalendarApiClient;
import disaster.service.calendar.CalendarEventService;
import disaster.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
public class GoogleCalendarService implements CalendarEventService {

    private final GoogleCalendarApiClient apiClient;
    private final CalendarEventDao calendarEventDao;

    public Flux<CalendarEvent> getCalendarEventsByUpdateReason(NotificationUpdateReason reason, CalendarSearchDto searchDto) {
        switch (reason) {
            case CALENDAR_INTERVAL_EVENT:
                return getCalendarEventsBySearchDto(searchDto)
                    .collectList()
                    .flatMap((lst) -> saveCalendarEvents(searchDto.getCalendarId(), lst)
                        .thenReturn(lst))
                    .flatMapIterable((entry) -> entry);
            case REPOSITORY_UPDATE_EVENT:
                return getCalendarEventsFromSaved(searchDto);
            default:
                return Flux.empty();
        }
    }

    public Flux<NotificationUpdateReason> getCalendarUpdateNotifier() {
        return Flux.interval(Duration.ZERO, Duration.ofMinutes(10))
            .map((event) -> NotificationUpdateReason.CALENDAR_INTERVAL_EVENT);
    }

    private Flux<CalendarEvent> getCalendarEventsBySearchDto(CalendarSearchDto searchDto) {
        if (!isValidSearchBounds(searchDto.getTimeBounds())) {
            var exception = new CalendarInvalidBounds();
            return Flux.error(exception);
        }
        return apiClient
            .getEventsByBounds(searchDto)
            .filter(this::isWithLocation);
    }

    private Flux<CalendarEvent> getCalendarEventsFromSaved(CalendarSearchDto searchDto) {
        return calendarEventDao.getCalendarEventByBounds(
            searchDto.getCalendarId(),
            searchDto.getTimeBounds()
        );
    }

    private Mono<Void> saveCalendarEvents(String calendarId, List<CalendarEvent> events) {
        return calendarEventDao.upsertCalendarEvents(calendarId, events);
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
