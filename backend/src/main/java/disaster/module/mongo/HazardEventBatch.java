package disaster.module.mongo;

import disaster.model.disasters.HazardEvent;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "HazardEventBatch")
public class HazardEventBatch {

    @Id
    private String id;

    private List<HazardEvent> items;

    @Indexed
    private BatchStatusType status;

    @CreatedDate
    private LocalDateTime createdAt;
}
