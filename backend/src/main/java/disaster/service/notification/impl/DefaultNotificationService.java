package disaster.service.notification.impl;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.disaster.DisasterEvent;
import disaster.model.notification.DisasterNotification;
import disaster.model.notification.NotificationUpdateReason;
import disaster.service.calendar.CalendarEventService;
import disaster.service.disaster.DisasterEventService;
import disaster.service.notification.NotificationService;
import disaster.service.stats.FrequencyStatsService;
import disaster.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultNotificationService implements NotificationService {

    private final CalendarEventService calendarEventsService;
    private final DisasterEventService disasterEventService;

    public Flux<DisasterNotification> getNotificationsBySearchDto(CalendarSearchDto searchDto) {
        return getUpdateReasonStream()
            .flatMap((eventType) -> disasterEventService.getDisasterEventsByBounds(searchDto.getTimeBounds())
                .collectList()
                .flatMapMany((lst) -> {
                        String integrity = CryptoUtils.generateIntegrityId();
                        return calendarEventsService.getCalendarEventsByUpdateReason(eventType, searchDto)
                            .flatMap((event) -> mapNotifications(event, lst, integrity));
                    }
                )
            );
    }

    private Mono<DisasterNotification> mapNotifications(CalendarEvent calendarEvent, List<DisasterEvent> disasters, String integrity) {
        return Flux.fromIterable(disasters)
            .filter((disaster) -> Objects.equals(disaster.getLocation(), calendarEvent.getLocation()))
            .collectList()
            .map((lst) -> new DisasterNotification(integrity, calendarEvent, lst))
            .filter((notification) -> !notification.getDisasterEvents().isEmpty());
    }

    private Flux<NotificationUpdateReason> getUpdateReasonStream() {
        return Flux.merge(
            disasterEventService.getRepositoryUpdateNotifier(),
            calendarEventsService.getCalendarUpdateNotifier()
        );
    }
}
