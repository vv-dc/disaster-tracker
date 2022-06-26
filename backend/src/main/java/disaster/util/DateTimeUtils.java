package disaster.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {

    public static String formatToISODateTime(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static boolean isAfterNow(ZonedDateTime dateTime) {
        var shiftedNow = ZonedDateTime.now().minus(1, ChronoUnit.DAYS);
        return dateTime.isAfter(shiftedNow);
    }

    public static boolean isBoundsValid(ZonedDateTime min, ZonedDateTime max) {
        return min.isBefore(max);
    }
}
