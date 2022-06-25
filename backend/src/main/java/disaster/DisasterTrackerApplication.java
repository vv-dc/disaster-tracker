package disaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
class DisasterTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterTrackerApplication.class, args);
    }
}
