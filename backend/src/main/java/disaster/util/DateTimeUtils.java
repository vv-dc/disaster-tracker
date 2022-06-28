package disaster.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
    private static final ZoneId zoneId = ZoneId.of("UTC");
    private static final ZoneOffset zoneOffset = ZoneOffset.UTC;

    public static String formatToISODateTime(LocalDateTime dateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofLocal(dateTime, zoneId, zoneOffset);
        return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static boolean isAfterNow(LocalDateTime dateTime) {
        var shiftedNow = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        return dateTime.isAfter(shiftedNow);
    }

    public static boolean isBoundsValid(LocalDateTime min, LocalDateTime max) {
        return min.isBefore(max);
    }

    public static LocalDateTime parseToLocal(String dateTimeString) {
        var instant = Instant.parse(dateTimeString);
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime toLocalFromZoned(ZonedDateTime dateTime, String timeZone) {
        var zoneId = ZoneId.of(timeZone);
        return dateTime.withZoneSameInstant(zoneId).toLocalDateTime();
    }
}
