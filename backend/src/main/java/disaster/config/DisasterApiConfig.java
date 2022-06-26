package disaster.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DisasterApiConfig {

    @Value("${disaster.integration.apis.disaster-alert.base-url}")
    private String disasterAlertApiUrl;

    @Value("${disaster.integration.apis.eonet.base-url}")
    private String eonetApiUrl;

    @Value("${disaster.integration.apis.openstreetmap.base-url}")
    private String openStreetMapApiUrl;
}
