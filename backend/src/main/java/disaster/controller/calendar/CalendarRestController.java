package disaster.controller.calendar;

import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchBounds;
import disaster.model.calendar.CalendarSearchDto;
import disaster.service.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

@RestController
public class CalendarRestController {

    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public CalendarRestController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping("/api/events/google")
    public Flux<CalendarEvent> getGoogleEvents(
        ServerWebExchange exchange,
        @RequestParam(value = "calendarId") String calendarId,
        @RequestParam(value = "timeMin") String timeMin,
        @RequestParam(value = "timeMax") String timeMax
    ) {
        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        var searchDto = new CalendarSearchDto();
        searchDto.setCalendarId(calendarId);
        searchDto.setAccessToken(accessToken);
        searchDto.setTimeBounds(new CalendarSearchBounds(timeMin, timeMax));
        return googleCalendarService.getEventsByBoundsWithLocation(searchDto);
    }
}
