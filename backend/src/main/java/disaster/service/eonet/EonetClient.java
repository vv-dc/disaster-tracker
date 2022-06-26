package disaster.service.eonet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import disaster.model.disasters.HazardEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class EonetClient {

    public static final String API_URL = "https://eonet.gsfc.nasa.gov/api/v3";
    public static final WebClient webclient = WebClient.create(API_URL);
    private final ObjectMapper mapper = new ObjectMapper();

    public Flux<HazardEvent> getEvents() {
        return webclient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events")
                        .queryParam("status", "open")
                        .build()
                )
                .exchangeToFlux(response ->
                        response
                                .bodyToFlux(String.class)
                                .concatMap(
                                        body -> {
                                            try {
                                                return Flux.fromArray(mapper.readValue(body, HazardEvent[].class)
                                                );
                                            } catch (JsonProcessingException e) {
                                                return Flux.error(e);
                                            }
                                        }
                                )
                );
    }
}
