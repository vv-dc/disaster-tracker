package disaster.model.disasters;

import disaster.model.geocoding.GeocodingResult;
import disaster.model.geocoding.SuccessGeocodingResult;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "hazard-events")
public class HazardEvent {
    private String hazardType;
    private String startTime;
    private double longitude;
    private double latitude;
    private String location;

    public static HazardEvent fromDto(HazardEventApiDto dto, GeocodingResult location) {
        var result = new HazardEvent();
        result.setLatitude(dto.getLatitude());
        result.setLongitude(dto.getLongitude());
        result.setHazardType(dto.getHazardType());
        result.setStartTime(dto.getStartTime());
        result.setLocation(((SuccessGeocodingResult) location).getName());
        return result;
    }
}
