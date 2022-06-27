package disaster.module.mongo;

import disaster.model.calendar.CalendarEvent;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Document(collection = "CalendarEvent")
@Data
public class CalendarEventBatch {

    @Id
    private String id;

    @Indexed
    private String calendarId;

    private Flux<CalendarEvent> items;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
