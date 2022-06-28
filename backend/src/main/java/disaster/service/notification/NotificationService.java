package disaster.service.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.disasters.HazardEvent;
import disaster.module.event.DisasterEventType;
import disaster.service.calendar.GoogleCalendarService;
import disaster.service.disaster.DisasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@AllArgsConstructor
public class NotificationService {

    private final GoogleCalendarService googleCalendarService;
    private final DisasterService disasterService;

    public Flux<HazardEvent> getNotificationsStream(CalendarSearchDto searchDto) {
        return getEventTypeStream()
            .flatMap((eventType) -> disasterService.getDisastersByCalendarEvents(
                getCalendarEventsByEventType(eventType, searchDto))
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

    private Flux<DisasterEventType> getEventTypeStream() {
        return Flux.merge(
            Flux.interval(Duration.ZERO, Duration.ofMinutes(1))
                    .map((i) -> DisasterEventType.CALENDAR_INTERVAL_EVENT),
            disasterService.getEventFlux()
        );
    }
}
