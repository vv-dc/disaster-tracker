package disaster.module.mongo;

import disaster.model.calendar.CalendarEvent;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "CalendarEventBatch")
@Data
public class CalendarEventBatch {

    @Id
    private String id;

    @Indexed(unique = true)
    private String calendarId;

    private List<CalendarEvent> items;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
