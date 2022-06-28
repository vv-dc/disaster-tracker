package disaster.model.disaster;

import disaster.model.geocoding.GeocodingResult;
import disaster.model.geocoding.SuccessGeocodingResult;
import disaster.util.DateTimeUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
public class DisasterEvent {

    private DisasterEventType hazardType;
    private LocalDateTime startTime;
    private double longitude;
    private double latitude;
    private String location;
    private DisasterEventSource source;

    public static DisasterEvent fromDto(DisasterEventRawDto dto, GeocodingResult location, DisasterEventSource source) {
        return DisasterEvent.builder()
            .source(source)
            .latitude(dto.getLatitude())
            .longitude(dto.getLongitude())
            .hazardType(dto.getHazardType())
            .startTime(DateTimeUtils.parseToLocal(dto.getStartTime()))
            .location(((SuccessGeocodingResult) location).getName())
            .build();
    }
}
