package disaster.service.geocoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import disaster.model.geocoding.GeocodingResult;
import disaster.model.mappers.OpenStreetMapGeocodeResultDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenStreetMapGeolocationService {

    public static final String API_URL = "https://nominatim.openstreetmap.org";
    public static final WebClient webclient = WebClient.create(API_URL);
    public final ObjectMapper mapper;

    public OpenStreetMapGeolocationService() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(GeocodingResult.class, new OpenStreetMapGeocodeResultDeserializer());
        mapper.registerModule(module);
    }

    public Mono<GeocodingResult> locate(double latitude, double longitude) {
        return webclient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reverse")
                        .queryParam("format", "json")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build()
                )
                .exchangeToMono(response -> {
                    if (!response.statusCode().is5xxServerError()) {
                        return response
                                .bodyToMono(String.class)
                                .map(body -> {
                                    try {
                                        return mapper.readValue(body, GeocodingResult.class);
                                    } catch (JsonProcessingException e) {
                                        return GeocodingResult.error(e.getMessage());
                                    }
                                });
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });
    }
}
