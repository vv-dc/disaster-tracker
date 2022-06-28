package disaster.model.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import disaster.model.disaster.DisasterEventRawDto;
import disaster.model.disaster.DisasterEventType;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class EonetHazardEventDeserializer extends StdDeserializer<DisasterEventRawDto> {

    private final Map<String, DisasterEventType> hazardEventTypeMap = Map.of(
            "drought", DisasterEventType.DROUGHT,
            "earthquakes", DisasterEventType.EARTHQUAKE,
            "floods", DisasterEventType.FLOOD,
            "landslides", DisasterEventType.LANDSLIDE,
            "severe storms", DisasterEventType.STORM,
            "volcanoes", DisasterEventType.VOLCANO,
            "wildfires", DisasterEventType.WILDFIRE
    );

    public EonetHazardEventDeserializer() {
        this(null);
    }

    public EonetHazardEventDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DisasterEventRawDto deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);


        var type = getHazardEventType(node.get("categories").get(0).get("title").textValue());
        var geometryNode = node.get("geometry").get(0);
        var startDate = geometryNode.get("date").asText();
        var longitude = geometryNode.get("coordinates").get(0).asDouble();
        var latitude = geometryNode.get("coordinates").get(1).asDouble();

        var hazardEvent = new DisasterEventRawDto();
        hazardEvent.setHazardType(type);
        hazardEvent.setStartTime(startDate);
        hazardEvent.setLongitude(longitude);
        hazardEvent.setLatitude(latitude);
        return hazardEvent;
    }

    private DisasterEventType getHazardEventType(String string) {
        var key = string.toLowerCase(Locale.ROOT);
        if (hazardEventTypeMap.containsKey(key))
            return hazardEventTypeMap.get(key);
        return DisasterEventType.UNKNOWN;
    }
}
