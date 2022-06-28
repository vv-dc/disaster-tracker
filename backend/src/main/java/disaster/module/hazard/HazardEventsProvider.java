package disaster.module.hazard;

import disaster.model.disasters.HazardEvent;
import disaster.module.hazard.disasteralert.DisasterAlertApiClient;
import disaster.module.hazard.eonet.EonetApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class HazardEventsProvider {

    private final DisasterAlertApiClient disasterAlertApiClient;
    private final EonetApiClient eonetApiClient;

    @Autowired
    public HazardEventsProvider(
        DisasterAlertApiClient disasterAlertApiClient,
        EonetApiClient eonetApiClient
    ) {
        this.disasterAlertApiClient = disasterAlertApiClient;
        this.eonetApiClient = eonetApiClient;
    }

    public Flux<HazardEvent> composeHazardEvents() {
        return Flux.empty();
//        return Flux.merge(
//               disasterAlertApiClient.getEvents(),
//               eonetApiClient.getEvents()
//            )
//            .log();
    }
}
