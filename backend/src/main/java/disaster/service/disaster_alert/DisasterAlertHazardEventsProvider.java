package disaster.service.disaster_alert;

import disaster.model.disasters.HazardEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class DisasterAlertHazardEventsProvider {

    private final Flux<HazardEvent> reactiveEventProvider;

    @Autowired
    public DisasterAlertHazardEventsProvider(DisasterAlertClient client) {
        reactiveEventProvider = Flux.interval(Duration.ofMinutes(1))
                .concatMap(i -> client.getEvents());
    }

    public Flux<HazardEvent> eventStream() {
        return reactiveEventProvider;
    }
}
