package disaster.repository;

import disaster.model.disasters.HazardEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveHazardEventRepository extends ReactiveMongoRepository<HazardEvent, String> {

    Flux<HazardEvent> getHazardEventByLocationIn(Flux<String> locations);

}
