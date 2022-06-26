package disaster.model.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import disaster.model.geocoding.GeocodingResult;

import java.io.IOException;

public class OpenStreetMapGeocodeResultDeserializer extends StdDeserializer<GeocodingResult> {
    public OpenStreetMapGeocodeResultDeserializer() {
        this(null);
    }

    public OpenStreetMapGeocodeResultDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GeocodingResult deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        var error = node.path("error");
        if (!error.isMissingNode()) {
            return GeocodingResult.error(error.textValue());
        }

        var addressNode = node.get("address");
        String name = "";
        var cityNode = addressNode.path("city");
        if (!cityNode.isMissingNode()) {
            name = cityNode.textValue();
        }

        var villageNode = addressNode.path("village");
        if (!villageNode.isMissingNode()) {
            name = villageNode.textValue();
        }
        //var state = addressNode.get("state").textValue();
        var country = addressNode.get("country").textValue();
        return GeocodingResult.success(name, null
                , country);
    }
}
