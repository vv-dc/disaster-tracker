package disaster.dao.calendar;

import disaster.model.calendar.CalendarEvent;
import disaster.model.common.TimeSearchBounds;
import disaster.module.mongo.CalendarEventBatch;
import disaster.repository.CalendarEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class CalendarEventDao {

    private final CalendarEventRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<Void> upsertCalendarEvents(String calendarId, List<CalendarEvent> events) {
        Query query = new Query();
        query.addCriteria(Criteria.where("calendarId").is(calendarId));

        Update update = new Update();
        update.set("items", events);
        update.set("updatedAt", LocalDateTime.now());

        return mongoTemplate.upsert(query, update, CalendarEventBatch.class)
            .doOnNext((res) -> log.info("Updated events for calendar: " + calendarId))
            .then();
    }

    public Flux<CalendarEvent> getCalendarEventByBounds(String calendarId, TimeSearchBounds bounds) {
        return repository.findByIdAndBounds(calendarId, bounds.getTimeMin(), bounds.getTimeMax())
            .flatMapIterable(CalendarEventBatch::getItems);
    }
}
