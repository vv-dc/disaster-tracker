package disaster.repository;

import disaster.model.stats.StatsRawEntry;
import disaster.module.mongo.DisasterEventBatch;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FrequencyStatsRepository extends ReactiveMongoRepository<DisasterEventBatch, String> {

    @Aggregation(pipeline = {
        "{ $match: { status: 'DISABLED' } }",
        "{ $unwind: '$items' }",
        "{ $group: { "
            + "_id: { id: '$items.location', term: '$items.location' },"
            + "frequency: { $sum: 1 }"
            + "}}",
        "{ $group: {"
            + "_id: '$_id.id'"
            + "terms: { $push: { frequency: '$frequency' } }"
            + "}}"
    })
    Flux<StatsRawEntry> aggregateAllByLocation();
}
