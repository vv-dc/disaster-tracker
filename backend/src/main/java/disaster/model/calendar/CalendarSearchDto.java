package disaster.model.calendar;

import disaster.model.common.TimeSearchBounds;
import lombok.Data;

@Data
public class CalendarSearchDto {

    private String calendarId;
    private String accessToken;
    private TimeSearchBounds timeBounds;
}
