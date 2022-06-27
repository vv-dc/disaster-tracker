package disaster.model.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import disaster.model.disasters.HazardEvent;
import disaster.model.disasters.HazardEventApiDto;
import disaster.model.disasters.HazardEventType;

import java.io.IOException;

public class DisasterAlertHazardEventDeserializer extends StdDeserializer<HazardEventApiDto> {

    public DisasterAlertHazardEventDeserializer() {
        this(null);
    }

    public DisasterAlertHazardEventDeserializer(Class<HazardEvent> t) {
        super(t);
    }

    @Override
    public HazardEventApiDto deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        var startDate = node.get("start_Date").asText();
        var type = getHazardEventType(node.get("type_ID").asText());
        var longitude = node.get("longitude").asDouble();
        var latitude = node.get("latitude").asDouble();

        var hazardEvent = new HazardEventApiDto();
        hazardEvent.setHazardType(type);
        hazardEvent.setStartTime(startDate);
        hazardEvent.setLongitude(longitude);
        hazardEvent.setLatitude(latitude);
        return hazardEvent;
    }

    private HazardEventType getHazardEventType(String string) {
        if (string.equals("FLOOD"))
            return HazardEventType.FLOOD;
        if (string.equals("DROUGHT"))
            return HazardEventType.DROUGHT;
        if (string.equals("BIOMEDICAL"))
            return HazardEventType.BIOMEDICAL;
        if (string.equals("STORM"))
            return HazardEventType.STORM;
        if (string.equals("VOLCANO"))
            return HazardEventType.VOLCANO;
        if (string.equals("LANDSLIDE"))
            return HazardEventType.LANDSLIDE;
        if (string.equals("EARTHQUAKE"))
            return HazardEventType.EARTHQUAKE;
        if (string.equals("WILDFIRE"))
            return HazardEventType.WILDFIRE;
        return HazardEventType.UNKNOWN;
    }
}
