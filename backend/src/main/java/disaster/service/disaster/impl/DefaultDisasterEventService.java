package disaster.service.disaster.impl;

import disaster.dao.disaster.DisasterEventDao;
import disaster.model.common.TimeSearchBounds;
import disaster.model.disaster.DisasterEvent;
import disaster.model.notification.NotificationUpdateReason;
import disaster.module.disaster.DisasterEventsProvider;
import disaster.service.disaster.DisasterEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Service
@Slf4j
public class DefaultDisasterEventService implements DisasterEventService {

    private static final int BATCH_SIZE = 15;

    private final DisasterEventsProvider eventsProvider;
    private final DisasterEventDao eventDao;
    private final Sinks.Many<NotificationUpdateReason> batchUpdateSink;

    public DefaultDisasterEventService(
        DisasterEventsProvider eventsProvider,
        DisasterEventDao eventDao
    ) {
        this.eventsProvider = eventsProvider;
        this.eventDao = eventDao;
        this.batchUpdateSink = Sinks.many().multicast().directAllOrNothing();
    }

    public Flux<NotificationUpdateReason> getRepositoryUpdateNotifier() {
        return batchUpdateSink.asFlux();
    }

    public Flux<DisasterEvent> getDisasterEventsByBounds(TimeSearchBounds bounds) {
        return eventDao.getDisasterEventsByBounds(bounds);
    }

    public Mono<Void> initDisasterEventsUpdate() {
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
                .flatMap(eventDao::saveDisasterEventsBatch)
            )
            .then();
    }

    private Mono<Void> handleBatchUpdate() {
        return eventDao.setNewActive()
            .then(Mono.defer(() -> {
                batchUpdateSink.emitNext(
                    NotificationUpdateReason.REPOSITORY_UPDATE_EVENT,
                    Sinks.EmitFailureHandler.FAIL_FAST
                );
                return Mono.empty();
            }));
    }
}
