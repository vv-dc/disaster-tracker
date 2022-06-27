package disaster.repository;

import disaster.model.disasters.HazardEvent;
import disaster.module.mongo.HazardEventBatch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface HazardEventBatchRepository extends ReactiveMongoRepository<HazardEventBatch, String> {

}
