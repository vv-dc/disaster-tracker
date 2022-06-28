package disaster.module.hazard.disasteralert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import disaster.config.DisasterApiConfig;
import disaster.model.disasters.HazardEvent;
import disaster.model.disasters.HazardEventApiDto;
import disaster.model.geocoding.SuccessGeocodingResult;
import disaster.model.mappers.DisasterAlertHazardEventDeserializer;
import disaster.service.geocoding.OpenStreetMapGeolocationService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;

@Service
public class DisasterAlertApiClient {

    public static final WebClient webclient = WebClient.builder()
            .defaultHeader("Accept", MediaType.APPLICATION_XML_VALUE)
            .build();

    private final XmlMapper mapper;
    private final OpenStreetMapGeolocationService geolocationService;
    private final DisasterApiConfig disasterApiConfig;

    public DisasterAlertApiClient(OpenStreetMapGeolocationService geolocationService, DisasterApiConfig disasterApiConfig) {
        this.geolocationService = geolocationService;
        this.disasterApiConfig = disasterApiConfig;

        mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(HazardEventApiDto.class, new DisasterAlertHazardEventDeserializer());
        mapper.registerModule(module);
    }

    public Flux<HazardEvent> getEvents() {
        var uri = buildUri();
        return webclient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(body -> {
                            try {
                                return Flux.fromArray(mapper.readValue(body, HazardEventApiDto[].class)
                                );
                            } catch (JsonProcessingException e) {
                                return Flux.error(e);
                            }
                        }
                )
                .concatMap(dto -> geolocationService
                        .locate(dto.getLatitude(), dto.getLongitude())
                        .handle((location, s) -> {
                            if (location instanceof SuccessGeocodingResult) {
                                var fixedDate = dto.getStartTime().substring(0, 19) + "Z";
                                dto.setStartTime(fixedDate);
                                s.next(HazardEvent.fromDto(dto, location));
                            }
                        })
                );
    }

    private URI buildUri() {
        return UriComponentsBuilder
                .fromHttpUrl(disasterApiConfig.getDisasterAlertApiUrl())
                .build()
                .toUri();
    }
}
