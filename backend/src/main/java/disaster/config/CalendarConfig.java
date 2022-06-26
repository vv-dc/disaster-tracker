package disaster.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CalendarConfig {

    @Value("${disaster.integration.google.calendar.base-url}")
    private String googleCalendarApiUrl;
}
