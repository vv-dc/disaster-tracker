package disaster.module.disaster.eonet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import disaster.config.DisasterApiConfig;
import disaster.model.disaster.DisasterEvent;
import disaster.model.disaster.DisasterEventRawDto;
import disaster.model.disaster.DisasterEventSource;
import disaster.model.geocoding.SuccessGeocodingResult;
import disaster.model.mappers.EonetHazardEventDeserializer;
import disaster.service.geocoding.OpenStreetMapGeolocationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;

@Service
public class EonetApiClient {

    private final WebClient webclient = WebClient.create();
    private final ObjectMapper mapper;
    private final DisasterApiConfig disasterApiConfig;
    private final OpenStreetMapGeolocationService geolocationService;

    public EonetApiClient(DisasterApiConfig disasterApiConfig, OpenStreetMapGeolocationService geolocationService) {

        this.disasterApiConfig = disasterApiConfig;
        this.geolocationService = geolocationService;
        this.mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(DisasterEventRawDto.class, new EonetHazardEventDeserializer());
        this.mapper.registerModule(module);
    }

    public Flux<DisasterEvent> getEvents() {
        var uri = buildUri("open");
        return webclient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .flatMapMany(
                body -> {
                    try {
                        var jsonNode = mapper.readTree(body);
                        return Flux.fromArray(mapper.readValue(jsonNode.get("events").toString(), DisasterEventRawDto[].class)
                        );
                    } catch (JsonProcessingException e) {
                        return Flux.error(e);
                    }
                }
            ).concatMap(dto -> geolocationService
                .locate(dto.getLatitude(), dto.getLongitude())
                .handle((location, s) -> {
                    if (location instanceof SuccessGeocodingResult) {
                        s.next(DisasterEvent.fromDto(dto, location, DisasterEventSource.EONET));
                    }
                })
            );
    }

    private URI buildUri(String status) {
        return UriComponentsBuilder
            .fromHttpUrl(disasterApiConfig.getEonetApiUrl())
            .queryParam("status", status)
            .queryParam("limit", 5)
            .build()
            .toUri();
    }
}
