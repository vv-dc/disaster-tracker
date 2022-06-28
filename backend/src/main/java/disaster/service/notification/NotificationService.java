package disaster.service.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.disaster.DisasterEvent;
import disaster.model.notification.DisasterNotification;
import disaster.model.disaster.DisasterUpdateEventType;
import disaster.service.calendar.GoogleCalendarService;
import disaster.service.disaster.DisasterEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationService {

    private final GoogleCalendarService googleCalendarService;
    private final DisasterEventService disasterEventService;

    public Flux<DisasterNotification> getNotificationsStream(CalendarSearchDto searchDto) {
        return getEventTypeStream()
            .flatMap((eventType) -> disasterEventService.getDisasterEventsByBounds(searchDto.getTimeBounds())
                .collectList()
                .flatMapMany((lst) -> {
                    var integrity = generateIntegrity();
                    return getCalendarEventsByEventType(eventType, searchDto)
                            .flatMap((event) -> this.mapNotifications(event, lst, integrity));
                    }
                )
            );
    }

    private Flux<CalendarEvent> getCalendarEventsByEventType(DisasterUpdateEventType eventType, CalendarSearchDto searchDto) {
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
                return googleCalendarService.getEventsFromDatabase(searchDto);
        }
        return Flux.empty();
    }

    private Mono<DisasterNotification> mapNotifications(CalendarEvent calendarEvent, List<DisasterEvent> disasters, String integrity) {
        return Flux.fromIterable(disasters)
            .filter((disaster) -> Objects.equals(disaster.getLocation(), calendarEvent.getLocation()))
            .collectList()
            .map((lst) -> new DisasterNotification(integrity, calendarEvent, lst))
            .filter((notification) -> !notification.getDisasterEvents().isEmpty());
    }

    private Flux<DisasterUpdateEventType> getEventTypeStream() {
        return Flux.merge(
            Flux.interval(Duration.ZERO, Duration.ofMinutes(10))
                    .map((i) -> DisasterUpdateEventType.CALENDAR_INTERVAL_EVENT),
            disasterEventService.getEventFlux()
        );
    }

    private String generateIntegrity() {
        return UUID.randomUUID().toString();
    }
}
