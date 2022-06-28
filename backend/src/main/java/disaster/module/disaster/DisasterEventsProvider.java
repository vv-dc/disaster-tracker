package disaster.module.disaster;

import disaster.model.disaster.DisasterEvent;
import disaster.module.disaster.disasteralert.DisasterAlertApiClient;
import disaster.module.disaster.eonet.EonetApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class DisasterEventsProvider {

    private final DisasterAlertApiClient disasterAlertApiClient;
    private final EonetApiClient eonetApiClient;

    @Autowired
    public DisasterEventsProvider(
        DisasterAlertApiClient disasterAlertApiClient,
        EonetApiClient eonetApiClient
    ) {
        this.disasterAlertApiClient = disasterAlertApiClient;
        this.eonetApiClient = eonetApiClient;
    }

    public Flux<DisasterEvent> composeHazardEvents() {
        return Flux.merge(
                disasterAlertApiClient.getEvents(),
                eonetApiClient.getEvents()
            )
            .log();
    }
}
