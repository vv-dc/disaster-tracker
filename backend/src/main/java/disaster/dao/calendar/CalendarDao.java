package disaster.dao.calendar;

import disaster.model.calendar.CalendarEvent;
import disaster.module.mongo.CalendarEventBatch;
import disaster.repository.CalendarRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class CalendarDao {

    private final CalendarRepository repository;

    public Mono<Void> upsertCalendarEvents(String calendarId, List<CalendarEvent> events) {
        var calendarBatch = new CalendarEventBatch();
        calendarBatch.setCalendarId(calendarId);
        calendarBatch.setItems(events);

        return repository.save(calendarBatch)
            .doOnNext((res) -> log.info("Updated events for calendar: " + calendarId))
            .then();
    }

    public Flux<CalendarEvent> getCalendarEventByCalendarId(String calendarId) {
        return repository.findFirstByCalendarId(calendarId)
            .flatMapIterable(CalendarEventBatch::getItems);
    }
}
