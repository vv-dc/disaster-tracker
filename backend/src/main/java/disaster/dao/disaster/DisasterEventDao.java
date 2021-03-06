package disaster.dao.disaster;

import com.mongodb.client.result.UpdateResult;
import disaster.model.common.TimeSearchBounds;
import disaster.model.disaster.DisasterEvent;
import disaster.module.mongo.BatchStatusType;
import disaster.module.mongo.DisasterEventBatch;
import disaster.repository.DisasterEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class DisasterEventDao {

    private final DisasterEventRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;

    public DisasterEventDao(DisasterEventRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public Flux<DisasterEvent> getDisasterEventsByBounds(TimeSearchBounds bounds) {
        return repository.findAllByTopBound(bounds.getTimeMin())
            .doOnNext((res) -> log.info("Found " + res.getItems().size() + " disasters"))
            .flatMapIterable(DisasterEventBatch::getItems);
    }

    public Mono<UpdateResult> saveDisasterEventsBatch(List<DisasterEvent> events) {
        Query query = buildPendingBatchQuery();
        Update eventsUpdate = new Update();
        eventsUpdate.push("items").each(events);

        return mongoTemplate.updateFirst(query, eventsUpdate, DisasterEventBatch.class)
            .doOnNext((res) -> log.info("Added " + events.size() + " events to batch"));
    }

    public Mono<DisasterEventBatch> createNewBatch() {
        var nowTimestamp = LocalDateTime.now();

        var batch = new DisasterEventBatch();
        batch.setItems(List.of());
        batch.setStatus(BatchStatusType.PENDING);
        batch.setCreatedAt(nowTimestamp);

        return repository.save(batch)
            .doOnNext((res) -> log.info("Created new batch: " + nowTimestamp));
    }

    @Transactional
    public Mono<Void> setNewActive() {
        return setAllDisabled()
            .then(Mono.defer(this::setPendingAsActive))
            .then();
    }

    public Mono<UpdateResult> setPendingAsActive() {
        Query query = buildPendingBatchQuery();
        Update batchUpdate = new Update();
        batchUpdate.set("status", BatchStatusType.ACTIVE);

        return mongoTemplate.updateFirst(query, batchUpdate, DisasterEventBatch.class)
            .doOnNext((res) -> log.info("Set latest batch as active"));
    }

    public Mono<UpdateResult> setAllDisabled() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(BatchStatusType.ACTIVE));
        Update batchUpdate = new Update();
        batchUpdate.set("status", BatchStatusType.DISABLED);
        return mongoTemplate.updateMulti(query, batchUpdate, DisasterEventBatch.class)
            .doOnNext((res) -> log.info("Set all batches to " + BatchStatusType.DISABLED));
    }

    private Query buildPendingBatchQuery() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(BatchStatusType.PENDING));
        return query;
    }
}
