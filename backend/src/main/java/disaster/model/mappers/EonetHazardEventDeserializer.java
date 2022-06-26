package disaster.model.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import disaster.model.disasters.HazardEvent;

import java.io.IOException;
import java.time.LocalTime;

public class EonetHazardEventDeserializer extends StdDeserializer<HazardEvent> {

    public EonetHazardEventDeserializer() {
        this(null);
    }

    public EonetHazardEventDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public HazardEvent deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        var startDate = node.get("start_Date").asText();
        var type = node.get("categories").get(0).get("title").textValue();
        var geometryNode = node.get("geometry").get(0);
        var longitude = geometryNode.get("longitude").asDouble();
        var latitude = geometryNode.get("latitude").asDouble();

        var hazardEvent = new HazardEvent();
        hazardEvent.setHazardType(type);
        hazardEvent.setStartTime(startDate);
        hazardEvent.setLongitude(longitude);
        hazardEvent.setLatitude(latitude);
        return hazardEvent;
    }
}
