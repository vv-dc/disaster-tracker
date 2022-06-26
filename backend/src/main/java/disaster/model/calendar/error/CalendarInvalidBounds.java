package disaster.model.calendar.error;

public class CalendarInvalidBounds extends RuntimeException{

    public CalendarInvalidBounds() {
        super("Invalid search bounds passed");
    }
}
