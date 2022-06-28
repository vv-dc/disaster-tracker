package disaster.service.geocoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import disaster.config.DisasterApiConfig;
import disaster.model.geocoding.GeocodingResult;
import disaster.model.mappers.OpenStreetMapGeocodeResultDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.DecimalFormat;

@Service
@Slf4j
public class OpenStreetMapGeolocationService {

    private static final DecimalFormat df = new DecimalFormat("#.###");

    private final WebClient webclient = WebClient.create();
    private final ObjectMapper mapper;
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
                    log.info("Unable to geocode: (" + latitude + ", " + longitude + ")");
                    return GeocodingResult.error(e.getMessage());
                }
            });
    }

    private URI buildUri(double latitude, double longitude) {
        return UriComponentsBuilder
            .fromHttpUrl(disasterApiConfig.getOpenStreetMapApiUrl())
            .queryParam("format", "json")
            .queryParam("lat", df.format(latitude))
            .queryParam("lon", df.format(longitude))
            .queryParam("accept-language", "en")
            .build()
            .toUri();
    }
}
