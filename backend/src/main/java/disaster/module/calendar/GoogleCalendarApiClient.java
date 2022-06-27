package disaster.module.calendar;

import disaster.config.CalendarConfig;
import disaster.model.calendar.CalendarEvent;
import disaster.model.calendar.CalendarSearchBounds;
import disaster.model.calendar.CalendarSearchDto;
import disaster.model.calendar.error.CalendarGenericHttpError;
import disaster.model.calendar.google.GoogleApiErrorWrapper;
import disaster.model.calendar.google.GoogleCalendarEventsList;
import disaster.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class GoogleCalendarApiClient {

    private final CalendarConfig calendarConfig;
    private final WebClient webClient;

    @Autowired
    public GoogleCalendarApiClient(CalendarConfig calendarConfig) {
        this.calendarConfig = calendarConfig;
        this.webClient = WebClient.builder()
            .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Flux<CalendarEvent> getEventsByBounds(CalendarSearchDto searchDto) {
        URI searchURI = buildUrlWithBounds(searchDto.getCalendarId(), searchDto.getTimeBounds());
        return webClient
            .get()
            .uri(searchURI)
            .header("Authorization", searchDto.getAccessToken())
            .retrieve()
            .onStatus(HttpStatus::isError, this::handleApiError)
            .bodyToMono(GoogleCalendarEventsList.class)
            .flatMapIterable(GoogleCalendarEventsList::getItems);
    }

    private URI buildUrlWithBounds(String calendarId, CalendarSearchBounds bounds) {
        String baseUrl = buildBaseApiUrl(calendarId);
        return UriComponentsBuilder
            .fromHttpUrl(baseUrl)
            .queryParam("timeMin", DateTimeUtils.formatToISODateTime(bounds.getTimeMin()))
            .queryParam("timeMax", DateTimeUtils.formatToISODateTime(bounds.getTimeMax()))
            .queryParam("timeZone", bounds.getTimeMax().getZone())
            .build()
            .toUri();
    }

    private String buildBaseApiUrl(String calendarId) {
        return StringUtils.replace(
            calendarConfig.getGoogleCalendarApiUrl(),
            "{calendarId}",
            calendarId
        );
    }

    private Mono<CalendarGenericHttpError> handleApiError(ClientResponse response) {
        return response.bodyToMono(GoogleApiErrorWrapper.class)
            .map((wrapper) -> new CalendarGenericHttpError(
                wrapper.getError().getCode(),
                wrapper.getError().getStatus(),
                wrapper.getError().getMessage()
            ));
    }
}
