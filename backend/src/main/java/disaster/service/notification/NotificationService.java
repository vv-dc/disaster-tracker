package disaster.service.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.disasters.HazardEvent;
import disaster.model.notification.DisasterNotification;
import disaster.module.event.DisasterEventType;
import disaster.service.calendar.GoogleCalendarService;
import disaster.service.disaster.DisasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class NotificationService {

    private final GoogleCalendarService googleCalendarService;
    private final DisasterService disasterService;

    public Flux<DisasterNotification> getNotificationsStream(CalendarSearchDto searchDto) {
        return getEventTypeStream()
            .flatMap((eventType) -> disasterService.getDisasterEventsByBounds(searchDto.getTimeBounds())
                .collectList()
                .flatMapMany((lst) -> getCalendarEventsByEventType(eventType, searchDto)
                    .flatMap((event) -> this.mapNotifications(event, lst))
                )
            );
    }

    private Flux<CalendarEvent> getCalendarEventsByEventType(DisasterEventType eventType, CalendarSearchDto searchDto) {
        switch (eventType) {
            case CALENDAR_INTERVAL_EVENT:
                return googleCalendarService.getEventsByBoundsWithLocation(searchDto)
                    .collectList()
                    .flatMap((lst) ->
                        googleCalendarService.saveEventsToDatabase(searchDto.getCalendarId(), lst)
                            .thenReturn(lst)
                    )
                    .flatMapIterable((l) -> l);
            case REPOSITORY_UPDATE_EVENT:
                return googleCalendarService.getEventsFromDatabase(searchDto.getCalendarId());
        }
        return Flux.empty();
    }

    private Mono<DisasterNotification> mapNotifications(CalendarEvent calendarEvent, List<HazardEvent> disasters) {
        return Flux.fromIterable(disasters)
            .filter((disaster) -> Objects.equals(disaster.getLocation(), calendarEvent.getLocation()))
            .collectList()
            .map((lst) -> new DisasterNotification(calendarEvent, lst))
            .filter((notification) -> !notification.getDisasterEvents().isEmpty());
    }

    private Flux<DisasterEventType> getEventTypeStream() {
        return Flux.merge(
            Flux.interval(Duration.ZERO, Duration.ofMinutes(1))
                    .map((i) -> DisasterEventType.CALENDAR_INTERVAL_EVENT),
            disasterService.getEventFlux()
        );
    }
}
