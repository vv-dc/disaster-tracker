package disaster.service.disaster_alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import disaster.model.disasters.HazardEvent;
import disaster.model.mappers.DisasterAlertHazardEventDeserializer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class DisasterAlertClient {

    public static final String API_URL = "https://hpxml.pdc.org";

    public static final WebClient webclient = WebClient.builder()
            .baseUrl(API_URL)
            .defaultHeader("Accept", MediaType.APPLICATION_XML_VALUE)
            .build();

    private final XmlMapper mapper;

    public DisasterAlertClient() {
        mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(HazardEvent.class, new DisasterAlertHazardEventDeserializer());
        mapper.registerModule(module);
    }

    public Flux<HazardEvent> getEvents() {
        return webclient.get()
                .uri("/public.xml")
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(body -> {
                            try {
                                return Flux.fromArray(mapper.readValue(body, HazardEvent[].class)
                                );
                            } catch (JsonProcessingException e) {
                                return Flux.error(e);
                            }
                        }
                );
    }

}
