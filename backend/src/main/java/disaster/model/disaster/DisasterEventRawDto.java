package disaster.model.disaster;

import lombok.Data;

@Data
public class DisasterEventRawDto {
    private DisasterEventType hazardType;
    private String startTime;
    private double longitude;
    private double latitude;
}
