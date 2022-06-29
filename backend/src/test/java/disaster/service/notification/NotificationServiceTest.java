package disaster.service.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.common.TimeSearchBounds;
import disaster.model.disaster.DisasterEvent;
import disaster.model.disaster.DisasterEventSource;
import disaster.model.disaster.HazardEventType;
import disaster.model.notification.NotificationUpdateReason;
import disaster.service.calendar.CalendarEventService;
import disaster.service.disaster.DisasterEventService;
import disaster.service.notification.impl.DefaultNotificationService;
import disaster.util.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

class NotificationServiceTest {

    @Test
    void shouldFilterDisasterEventsByLocation() {
        var searchDto = generateCalendarSearchDto();

        CalendarEventService calendarEventService = Mockito.mock(CalendarEventService.class);
        Mockito.when(calendarEventService.getCalendarUpdateNotifier())
            .thenReturn(Flux.just(NotificationUpdateReason.REPOSITORY_UPDATE_EVENT));
        Mockito.when(calendarEventService.getCalendarEventsByUpdateReason(NotificationUpdateReason.REPOSITORY_UPDATE_EVENT, searchDto))
            .thenReturn(Flux.just(
                generateCalendarEvent("1", "Mock1"),
                generateCalendarEvent("2", "Mock2"),
                generateCalendarEvent("3", "Mock3"),
                generateCalendarEvent("4", "Mock4")
            ));

        DisasterEventService disasterEventService = Mockito.mock(DisasterEventService.class);
        Mockito.when(disasterEventService.getRepositoryUpdateNotifier()).thenReturn(Flux.empty());
        Mockito.when(disasterEventService.getDisasterEventsByBounds(searchDto.getTimeBounds()))
            .thenReturn(Flux.just(
                generateDisasterEvent("Mock1"),
                generateDisasterEvent("Mock1"),
                generateDisasterEvent("Mock2"),
                generateDisasterEvent("Mock3")
            ));

        var notificationService = new DefaultNotificationService(calendarEventService, disasterEventService);
        var flux = notificationService.getNotificationsBySearchDto(searchDto);

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNextMatches((notification) ->
                Objects.equals(notification.getCalendarEvent().getLocation(), "Mock1") &&
                    notification.getDisasterEvents().size() == 2
            )
            .expectNextMatches((notification) ->
                Objects.equals(notification.getCalendarEvent().getLocation(), "Mock2") &&
                    notification.getDisasterEvents().size() == 1
            )
            .expectNextMatches((notification) ->
                Objects.equals(notification.getCalendarEvent().getLocation(), "Mock3") &&
                    notification.getDisasterEvents().size() == 1
            )
            .expectComplete()
            .verify();

    }

    @Test
    void shouldUseIntegrityForNotificationBatch() {
        var searchDto = generateCalendarSearchDto();
        Flux<CalendarEvent> calendarIntervalFlux = Flux.just(
            generateCalendarEvent("1", "Mock1"),
            generateCalendarEvent("2", "Mock2")
        );

        CalendarEventService calendarEventService = Mockito.mock(CalendarEventService.class);
        Mockito.when(calendarEventService.getCalendarUpdateNotifier())
            .thenReturn(Flux.just(
                NotificationUpdateReason.CALENDAR_INTERVAL_EVENT,
                NotificationUpdateReason.CALENDAR_INTERVAL_EVENT
            ));
        Mockito.when(calendarEventService.getCalendarEventsByUpdateReason(NotificationUpdateReason.CALENDAR_INTERVAL_EVENT, searchDto))
            .thenReturn(calendarIntervalFlux);
        Mockito.when(calendarEventService.getCalendarEventsByUpdateReason(NotificationUpdateReason.REPOSITORY_UPDATE_EVENT, searchDto)).thenReturn(Flux.empty());

        DisasterEventService disasterEventService = Mockito.mock(DisasterEventService.class);
        Mockito.when(disasterEventService.getRepositoryUpdateNotifier()).thenReturn(Flux.empty());
        Mockito.when(disasterEventService.getDisasterEventsByBounds(searchDto.getTimeBounds()))
            .thenReturn(Flux.just(
                generateDisasterEvent("Mock1"),
                generateDisasterEvent("Mock2")
            ));

        var mockedUtils = Mockito.mockStatic(CryptoUtils.class);
        mockedUtils.when(CryptoUtils::generateIntegrityId)
            .thenReturn("first-integrity")
            .thenReturn("second-integrity");

        var notificationService = new DefaultNotificationService(calendarEventService, disasterEventService);
        var flux = notificationService.getNotificationsBySearchDto(searchDto);

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNextMatches((notification) -> Objects.equals(notification.getIntegrity(), "first-integrity"))
            .expectNextMatches((notification) -> Objects.equals(notification.getIntegrity(), "first-integrity"))
            .expectNextMatches((notification) -> Objects.equals(notification.getIntegrity(), "second-integrity"))
            .expectNextMatches((notification) -> Objects.equals(notification.getIntegrity(), "second-integrity"))
            .expectComplete()
            .verify();
    }

    @Test
    void shouldComposeNotificationsForEveryUpdateReason() {
        var searchDto = generateCalendarSearchDto();
        Flux<CalendarEvent> calendarIntervalFlux = Flux.just(
            generateCalendarEvent("11", "Mock1")
        );
        Flux<CalendarEvent> repositoryUpdateFlux = Flux.just(
            generateCalendarEvent("21", "Mock2")
        );

        CalendarEventService calendarEventService = Mockito.mock(CalendarEventService.class);
        Mockito.when(calendarEventService.getCalendarUpdateNotifier())
            .thenReturn(Flux.just(
                NotificationUpdateReason.CALENDAR_INTERVAL_EVENT,
                NotificationUpdateReason.CALENDAR_INTERVAL_EVENT
            ));
        Mockito.when(calendarEventService.getCalendarEventsByUpdateReason(NotificationUpdateReason.CALENDAR_INTERVAL_EVENT, searchDto))
            .thenReturn(calendarIntervalFlux);
        Mockito.when(calendarEventService.getCalendarEventsByUpdateReason(NotificationUpdateReason.REPOSITORY_UPDATE_EVENT, searchDto))
            .thenReturn(repositoryUpdateFlux);

        DisasterEventService disasterEventService = Mockito.mock(DisasterEventService.class);
        Mockito.when(disasterEventService.getRepositoryUpdateNotifier())
            .thenReturn(Flux.just(
                NotificationUpdateReason.REPOSITORY_UPDATE_EVENT,
                NotificationUpdateReason.REPOSITORY_UPDATE_EVENT
            ));
        Mockito.when(disasterEventService.getDisasterEventsByBounds(searchDto.getTimeBounds()))
            .thenReturn(Flux.just(
                generateDisasterEvent("Mock1"),
                generateDisasterEvent("Mock2")
            ));

        var notificationService = new DefaultNotificationService(calendarEventService, disasterEventService);
        var flux = notificationService.getNotificationsBySearchDto(searchDto);

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNextCount(4) // 1 match for every event
            .expectComplete()
            .verify();
    }

    private DisasterEvent generateDisasterEvent(String location) {
        return DisasterEvent.builder()
            .hazardType(HazardEventType.CYCLONE)
            .source(DisasterEventSource.DISASTER_ALERT)
            .latitude(111)
            .longitude(222)
            .startTime(LocalDateTime.now())
            .location(location)
            .build();
    }

    private CalendarSearchDto generateCalendarSearchDto() {
        var calendarSearchDto = new CalendarSearchDto();
        calendarSearchDto.setCalendarId("disaster-tracker42");
        calendarSearchDto.setTimeBounds(generateTimeSearchBounds());
        calendarSearchDto.setAccessToken("access-token42");
        return calendarSearchDto;
    }

    private TimeSearchBounds generateTimeSearchBounds() {
        return new TimeSearchBounds(
            LocalDateTime.now(),
            LocalDateTime.now().plus(1, ChronoUnit.DAYS)
        );
    }

    private CalendarEvent generateCalendarEvent(String id, String location) {
        return CalendarEvent.builder()
            .id(id)
            .eventType("default")
            .status("confirmed")
            .htmlLink("https://example.com?a=b")
            .summary("Some really nice event")
            .location(location)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plus(3, ChronoUnit.DAYS))
            .build();
    }
}
