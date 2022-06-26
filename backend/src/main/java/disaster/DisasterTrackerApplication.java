package disaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
class DisasterTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterTrackerApplication.class, args);
    }
}
