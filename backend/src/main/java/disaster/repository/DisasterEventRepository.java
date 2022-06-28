package disaster.repository;

import disaster.module.mongo.DisasterEventBatch;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface DisasterEventRepository extends ReactiveMongoRepository<DisasterEventBatch, String> {

    @Aggregation(pipeline = {
        "{ '$match': { status: 'ACTIVE', 'items.startTime': { $lt: ?0 } } }",
        "{ '$project': {"
            + "items: { $filter: {"
            + "  input: '$items'"
            + "  as: 'item'"
            + "  cond: {"
            + "    $lt: ['$$item.startTime', ?0]"
            + "  }"
            + "}"
            + "}}}"
    })
    Flux<DisasterEventBatch> findAllByTopBound(LocalDateTime bound);
}
