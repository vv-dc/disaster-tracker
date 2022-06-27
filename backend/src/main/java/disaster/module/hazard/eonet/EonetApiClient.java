package disaster.module.hazard.eonet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import disaster.config.DisasterApiConfig;
import disaster.model.disasters.HazardEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;

@Service
public class EonetApiClient {

    public static final WebClient webclient = WebClient.create();
    private final ObjectMapper mapper = new ObjectMapper();
    private final DisasterApiConfig disasterApiConfig;

    public EonetApiClient(DisasterApiConfig disasterApiConfig) {

        this.disasterApiConfig = disasterApiConfig;
    }

    public Flux<HazardEvent> getEvents() {
        return Flux.empty(); // TODO: fixme
//        var uri = buildUri("open");
//        return webclient.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(String.class)
//                .flatMapMany(
//                        body -> {
//                            try {
//                                return Flux.fromArray(mapper.readValue(body, HazardEvent[].class)
//                                );
//                            } catch (JsonProcessingException e) {
//                                return Flux.error(e);
//                            }
//                        }
//                );
    }

    private URI buildUri(String status) {
        return UriComponentsBuilder
                .fromHttpUrl(disasterApiConfig.getEonetApiUrl())
                .queryParam("status", status)
                .build()
                .toUri();
    }
}
