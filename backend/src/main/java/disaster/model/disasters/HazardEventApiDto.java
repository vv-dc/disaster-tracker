package disaster.model.disasters;

import lombok.Data;

@Data
public class HazardEventApiDto {
    private String hazardType;
    private String startTime;
    private double longitude;
    private double latitude;
}
