package disaster.service.disaster;

import disaster.dao.hazard.HazardEventDao;
import disaster.model.common.TimeSearchBounds;
import disaster.model.disasters.HazardEvent;
import disaster.module.event.DisasterEventType;
import disaster.module.hazard.HazardEventsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Service
@Slf4j
public class DisasterService {

    private static final int BATCH_SIZE = 5;

    private final HazardEventsProvider eventsProvider;
    private final HazardEventDao eventDao;
    private final Sinks.Many<DisasterEventType> batchUpdateSink;

    public DisasterService(
        HazardEventsProvider eventsProvider,
        HazardEventDao eventDao
    ) {
        this.eventsProvider = eventsProvider;
        this.eventDao = eventDao;
        this.batchUpdateSink = Sinks.many().replay().all();
//        this.initEvents().subscribe();
    }

    public Flux<DisasterEventType> getEventFlux() {
        return batchUpdateSink.asFlux();
    }

    public Flux<HazardEvent> getDisasterEventsByBounds(TimeSearchBounds bounds) {
        return eventDao.getHazardEventsByBounds(bounds);
    }

    private Mono<Void> initEvents() {
        return Flux.interval(Duration.ZERO, Duration.ofMinutes(10))
            .concatMap((flux) ->
                eventDao.createNewBatch()
                    .then(Mono.defer(this::handleEvents))
                    .then(Mono.defer(this::handleBatchUpdate))
            )
            .then();
    }

    private Mono<Void> handleEvents() {
        return eventsProvider
            .composeHazardEvents()
            .window(BATCH_SIZE)
            .flatMap((flux) -> flux
                .collectList()
                .flatMap(eventDao::saveHazardEventsBatch)
            )
            .then();
    }

    private Mono<Void> handleBatchUpdate() {
        return eventDao.setNewActive()
            .then(Mono.defer(() -> {
                batchUpdateSink.emitNext(
                    DisasterEventType.REPOSITORY_UPDATE_EVENT,
                    Sinks.EmitFailureHandler.FAIL_FAST
                );
                return Mono.empty();
            }));
    }
}
