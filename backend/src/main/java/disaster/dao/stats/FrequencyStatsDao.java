package disaster.dao.stats;

import disaster.model.stats.FrequencyStatsEntry;
import disaster.module.mongo.BatchStatusType;
import disaster.module.mongo.DisasterEventBatch;
import disaster.module.mongo.FrequencyStats;
import disaster.repository.FrequencyStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FrequencyStatsDao {

    private final ReactiveMongoTemplate mongoTemplate;
    private final FrequencyStatsRepository repository;

    public Mono<FrequencyStats> getActiveFrequencyStats() {
        var query = new Query();
        query.addCriteria(Criteria.where("status").is(BatchStatusType.ACTIVE));
        return mongoTemplate.findOne(query, FrequencyStats.class);
    }

    @Transactional
    public Mono<Void> aggregateAllFrequencyStats() {
        return repository.aggregateAllByLocation()
            .map((entry) -> new FrequencyStatsEntry(
                entry.getId(),
                entry.getTerms().get(0).getFrequency()
            ))
            .collectList()
            .flatMap(this::rewriteFrequencyStats);
//            .then(Mono.defer(this::deleteOldEntries));
    }

    private Mono<Void> rewriteFrequencyStats(List<FrequencyStatsEntry> items) {
        var query = new Query();
        query.addCriteria(Criteria.where("status").is(BatchStatusType.ACTIVE));

        var update = new Update();
        update.set("updatedAt", LocalDateTime.now());
        update.set("items", items);

        return mongoTemplate.upsert(query, update, FrequencyStats.class)
            .doOnNext((res) -> log.info("Inserted new frequency stats"))
            .then();
    }

    private Mono<Void> deleteOldEntries() {
        var query = new Query();
        query.addCriteria(Criteria.where("status").is(BatchStatusType.DISABLED));

        return mongoTemplate.remove(query, DisasterEventBatch.class)
            .doOnNext((res) -> log.info("Deleted " + res.getDeletedCount() + " old entries"))
            .then();
    }
}
