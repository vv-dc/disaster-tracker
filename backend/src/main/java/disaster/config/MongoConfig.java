package disaster.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final MongoProperties mongoProperties;

    public MongoConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        var uri = mongoProperties.getUri();
        return MongoClients.create(uri);
    }

    @Override
    public String getDatabaseName() {
        return mongoProperties.getDatabase();
    }
}