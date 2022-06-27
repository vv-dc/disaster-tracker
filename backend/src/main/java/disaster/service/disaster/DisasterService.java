package disaster.service.disaster;

import disaster.model.disasters.HazardEvent;
import disaster.module.event.DisasterEventType;
import disaster.module.hazard.HazardEventsProvider;
import disaster.repository.ReactiveHazardEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class DisasterService {

   private final HazardEventsProvider eventsProvider;
   private final ReactiveHazardEventRepository eventRepository;
   private final Sinks.Many<DisasterEventType> repositorySink;

    @Autowired
    public DisasterService(
        HazardEventsProvider eventsProvider,
        ReactiveHazardEventRepository eventRepository
    ) {
        this.eventsProvider = eventsProvider;
        this.eventRepository = eventRepository;
        this.repositorySink = Sinks.many().replay().all();
        this.initEvents().subscribe();
    }

    public Flux<DisasterEventType> getEventFlux() {
        return repositorySink.asFlux();
    }

    private Flux<Void> initEvents() {
        return Flux.interval(Duration.ZERO, Duration.ofMinutes(10))
                .concatMap((flux) -> handleEvents());
    }

    private Mono<Void> handleEvents() {
        return eventsProvider
            .composeHazardEvents()
            .collectList()
            .map(this::rewriteRepository)
            .then(emitRepositoryUpdate());
    }

    private Disposable rewriteRepository(List<HazardEvent> events) {
        log.info("Saving " + events.size() + " events");
        return eventRepository
            .saveAll(events)
            .subscribe((result) -> log.info("Saved " + events.size() + " events"));
    }

    private Mono<Void> emitRepositoryUpdate() {
        repositorySink.emitNext(
            DisasterEventType.REPOSITORY_UPDATE_EVENT,
            Sinks.EmitFailureHandler.FAIL_FAST
        );
        return Mono.empty();
    }
}
