package disaster.model.disasters;

import disaster.model.geocoding.GeocodingResult;
import disaster.model.geocoding.SuccessGeocodingResult;
import disaster.util.DateTimeUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "HazardEvents")
public class HazardEvent {
    @Id
    private String id;
    private HazardEventType hazardType;
    private LocalDateTime startTime;
    private double longitude;
    private double latitude;
    private String location;

    public static HazardEvent fromDto(HazardEventApiDto dto, GeocodingResult location) {
        var result = new HazardEvent();
        result.setLatitude(dto.getLatitude());
        result.setLongitude(dto.getLongitude());
        result.setHazardType(dto.getHazardType());
        result.setStartTime(DateTimeUtils.parseToLocal(dto.getStartTime()));
        result.setLocation(((SuccessGeocodingResult) location).getName());
        return result;
    }
}
