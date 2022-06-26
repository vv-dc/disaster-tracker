package disaster.model.disasters;

import lombok.Data;

import java.time.LocalTime;

@Data
public class HazardEvent {
    private String hazardType;
    private String startTime;
    private double longitude;
    private double latitude;
}
