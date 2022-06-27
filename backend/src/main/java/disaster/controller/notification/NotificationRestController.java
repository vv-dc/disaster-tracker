package disaster.controller.notification;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchBounds;
import disaster.model.calendar.CalendarSearchDto;
import disaster.service.calendar.GoogleCalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public class NotificationRestController {

    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public NotificationRestController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping("/api/events")
    public Flux<CalendarEvent> getDisasterEvents(
        @RequestParam(value = "calendarId") String calendarId,
        @RequestParam(value = "timeMin") String timeMin,
        @RequestParam(value = "timeMax") String timeMax,
        @RequestParam(value = "accessToken") String accessToken
    ) {
        var searchDto = new CalendarSearchDto();
        searchDto.setCalendarId(calendarId);
        searchDto.setAccessToken("Bearer " + accessToken);
        searchDto.setTimeBounds(new CalendarSearchBounds(timeMin, timeMax));

        return googleCalendarService.getEventsByBoundsStream(searchDto);
    }
}
