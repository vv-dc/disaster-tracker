package disaster.model.disasters;

import lombok.Data;

@Data
public class HazardEventApiDto {
    private HazardEventType hazardType;
    private String startTime;
    private double longitude;
    private double latitude;
}
