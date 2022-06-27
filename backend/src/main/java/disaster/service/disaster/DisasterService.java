package disaster.service.disaster;

import disaster.module.event.DisasterEventType;
import disaster.module.hazard.HazardEventsProvider;
import disaster.repository.ReactiveHazardEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

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

    private Mono<Void> initEvents() {
        return eventsProvider
            .getHazardEventsStream()
            .collectList()
            .map((lst) -> {
                log.info("Saving " + lst.size() + " events");
                return eventRepository.saveAll(lst);
            })
            .then(emitRepositoryUpdate());
    }

    private Mono<Void> emitRepositoryUpdate() {
        repositorySink.emitNext(
            DisasterEventType.REPOSITORY_UPDATE_EVENT,
            Sinks.EmitFailureHandler.FAIL_FAST
        );
        return Mono.empty();
    }
}
