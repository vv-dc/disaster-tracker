package disaster.model.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import disaster.model.disaster.DisasterEvent;
import disaster.model.disaster.DisasterEventRawDto;
import disaster.model.disaster.HazardEventType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DisasterAlertHazardEventDeserializer extends StdDeserializer<DisasterEventRawDto> {

    public DisasterAlertHazardEventDeserializer() {
        this(null);
    }

    public DisasterAlertHazardEventDeserializer(Class<DisasterEvent> t) {
        super(t);
    }

    @Override
    public DisasterEventRawDto deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        var startDate = node.get("start_Date").asText();
        var type = getHazardEventType(node.get("type_ID").asText());
        var longitude = node.get("longitude").asDouble();
        var latitude = node.get("latitude").asDouble();

        var hazardEvent = new DisasterEventRawDto();
        hazardEvent.setHazardType(type);
        hazardEvent.setStartTime(startDate);
        hazardEvent.setLongitude(longitude);
        hazardEvent.setLatitude(latitude);
        return hazardEvent;
    }

    private HazardEventType getHazardEventType(String string) {
        try {
            return HazardEventType.valueOf(string);
        } catch (IllegalArgumentException ex) {
            log.warn("Unable to match hazardType: " + string);
            return HazardEventType.UNKNOWN;
        }
    }
}
