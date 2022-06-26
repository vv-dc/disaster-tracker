package disaster.service.eonet;

import disaster.model.disasters.HazardEvent;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class EonetHazardEventsProvider {
    private final Flux<HazardEvent> reactiveEventProvider;

    @Autowired
    public EonetHazardEventsProvider(EonetClient client) {
        reactiveEventProvider = Flux.interval(Duration.ZERO, Duration.ofMinutes(1))
                .concatMap(i -> client.getEvents());
    }

    public Flux<HazardEvent> eventStream() {
        return reactiveEventProvider;
    }
}