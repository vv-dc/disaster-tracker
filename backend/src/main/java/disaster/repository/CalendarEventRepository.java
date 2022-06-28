package disaster.repository;

import disaster.module.mongo.CalendarEventBatch;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CalendarEventRepository extends ReactiveMongoRepository<CalendarEventBatch, String> {

    @Aggregation(pipeline = {
        "{ '$match': { "
            + "$and: ["
            + "{ 'calendarId': ?0 },"
            + "{ 'items.start': { $lte: ?2 } },"
            + "{ 'items.end': { $gte: ?1 } }"
            + "]}}",
        "{ '$project': {"
            + "items: { $filter: {"
            + "  input: '$items'"
            + "  as: 'item'"
            + "  cond: ["
            + "    { $lte: ['$$item.start', ?2] },"
            + "    { $gte: ['$$item.end', ?1] }"
            + "  ]"
            + "}"
            + "}}}"
    })
    Mono<CalendarEventBatch> findByIdAndBounds(String calendarId, LocalDateTime start, LocalDateTime end);

}
