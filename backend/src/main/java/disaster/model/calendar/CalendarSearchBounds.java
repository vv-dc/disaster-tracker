package disaster.model.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarSearchBounds {

    private String timeMin; // kebab-case
    private String timeMax;
}
