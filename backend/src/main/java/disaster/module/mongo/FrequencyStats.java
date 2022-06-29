package disaster.module.mongo;

import disaster.model.stats.FrequencyStatsEntry;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "FrequencyStats")
@Data
public class FrequencyStats {

    @Id
    private String id;

    private List<FrequencyStatsEntry> items;

    private BatchStatusType status;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
