package disaster.service.geocoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import disaster.config.DisasterApiConfig;
import disaster.model.geocoding.GeocodingResult;
import disaster.model.mappers.OpenStreetMapGeocodeResultDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class OpenStreetMapGeolocationService {

    public static final WebClient webclient = WebClient.create();
    public final ObjectMapper mapper;
    private final DisasterApiConfig disasterApiConfig;

    public OpenStreetMapGeolocationService(DisasterApiConfig disasterApiConfig) {
        this.disasterApiConfig = disasterApiConfig;
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(GeocodingResult.class, new OpenStreetMapGeocodeResultDeserializer());
        mapper.registerModule(module);
    }

    public Mono<GeocodingResult> locate(double latitude, double longitude) {
        var uri = buildUri(latitude, longitude);
        return webclient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    try {
                        return mapper.readValue(body, GeocodingResult.class);
                    } catch (JsonProcessingException e) {
                        return GeocodingResult.error(e.getMessage());
                    }
                });
    }

    private URI buildUri(double latitude, double longitude) {
        return UriComponentsBuilder
                .fromHttpUrl(disasterApiConfig.getOpenStreetMapApiUrl())
                .queryParam("format", "json")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("accept-language", "en")
                .build()
                .toUri();
    }
}
