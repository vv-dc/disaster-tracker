package disaster.model.geocoding;

import lombok.Data;

@Data
public class ErrorGeocodingResult extends GeocodingResult {

    private String errorMessage;
}
