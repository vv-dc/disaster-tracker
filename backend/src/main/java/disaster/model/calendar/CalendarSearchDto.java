package disaster.model.calendar;

import lombok.Data;

@Data
public class CalendarSearchDto {

    private String calendarId;
    private String accessToken;
    private CalendarSearchBounds timeBounds;
}
