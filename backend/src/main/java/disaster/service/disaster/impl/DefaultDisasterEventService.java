package disaster.service.disaster.impl;

import disaster.dao.disaster.DisasterEventDao;
import disaster.model.common.TimeSearchBounds;
import disaster.model.disaster.DisasterEvent;
import disaster.model.disaster.DisasterEventSource;
import disaster.model.disaster.HazardEventType;
import disaster.model.notification.NotificationUpdateReason;
import disaster.model.stats.FrequencyStatsEntry;
import disaster.module.disaster.DisasterEventsProvider;
import disaster.service.disaster.DisasterEventService;
import disaster.service.stats.FrequencyStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultDisasterEventService implements DisasterEventService {

    private static final int BATCH_SIZE = 15;
    private static final int HOTTEST_DISASTERS_LIMIT = 10;

    private final DisasterEventsProvider eventsProvider;
    private final DisasterEventDao eventDao;
    private final FrequencyStatsService frequencyStatsService;

    private final Sinks.Many<NotificationUpdateReason> batchUpdateSink = Sinks.many().multicast().directAllOrNothing();

    public Flux<NotificationUpdateReason> getRepositoryUpdateNotifier() {
        return batchUpdateSink.asFlux();
    }

    public Flux<DisasterEvent> getDisasterEventsByBounds(TimeSearchBounds bounds) {
        return Flux.merge(
            eventDao.getDisasterEventsByBounds(bounds),
            getHottestDisasterEvents()
        );
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

    private Flux<DisasterEvent> getHottestDisasterEvents() {
        return frequencyStatsService.getHottestFrequencyStats(HOTTEST_DISASTERS_LIMIT)
            .map(this::mapFrequencyStatToDisasterEvent);
    }

    private DisasterEvent mapFrequencyStatToDisasterEvent(FrequencyStatsEntry entry) {
        return DisasterEvent.builder()
            .location(entry.getLocation())
            .startTime(null)
            .longitude(0)
            .latitude(0)
            .source(DisasterEventSource.FREQUENCY_STATS)
            .hazardType(HazardEventType.HOTTEST_POINT)
            .description("Frequency: " + entry.getFrequency())
            .build();
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
