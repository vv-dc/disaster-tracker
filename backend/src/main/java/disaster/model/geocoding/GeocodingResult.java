package disaster.model.geocoding;

import lombok.Data;

@Data
public abstract class GeocodingResult {

    public static GeocodingResult error(String errorMessage) {
        var result = new ErrorGeocodingResult();
        result.setErrorMessage(errorMessage);
        return result;
    }

    public static GeocodingResult success(String name, String state, String country) {
        var result = new SuccessGeocodingResult();
        result.setName(name);
        result.setState(state);
        result.setCountry(country);
        return result;
    }
}
