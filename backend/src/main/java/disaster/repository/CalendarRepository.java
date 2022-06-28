package disaster.repository;

import disaster.module.mongo.CalendarEventBatch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CalendarRepository extends ReactiveMongoRepository<CalendarEventBatch, String> {

    Mono<CalendarEventBatch> findFirstByCalendarId(String calendarId);
}
