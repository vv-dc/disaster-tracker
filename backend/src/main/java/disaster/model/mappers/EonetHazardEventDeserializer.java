package disaster.model.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import disaster.model.disasters.HazardEventApiDto;
import disaster.model.disasters.HazardEventType;

import java.io.IOException;
import java.util.Locale;

public class EonetHazardEventDeserializer extends StdDeserializer<HazardEventApiDto> {

    public EonetHazardEventDeserializer() {
        this(null);
    }

    public EonetHazardEventDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public HazardEventApiDto deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);


        var type = getHazardEventType(node.get("categories").get(0).get("title").textValue());
        var geometryNode = node.get("geometry").get(0);
        var startDate = geometryNode.get("date").asText();
        var longitude = geometryNode.get("coordinates").get(0).asDouble();
        var latitude = geometryNode.get("coordinates").get(1).asDouble();

        var hazardEvent = new HazardEventApiDto();
        hazardEvent.setHazardType(type);
        hazardEvent.setStartTime(startDate);
        hazardEvent.setLongitude(longitude);
        hazardEvent.setLatitude(latitude);
        return hazardEvent;
    }

    private HazardEventType getHazardEventType(String string) {
        if (string.toLowerCase(Locale.ROOT).equals("drought"))
            return HazardEventType.DROUGHT;
        if (string.toLowerCase(Locale.ROOT).equals("earthquakes"))
            return HazardEventType.EARTHQUAKE;
        if (string.toLowerCase(Locale.ROOT).equals("floods"))
            return HazardEventType.FLOOD;
        if (string.toLowerCase(Locale.ROOT).equals("landslides"))
            return HazardEventType.LANDSLIDE;
        if (string.toLowerCase(Locale.ROOT).equals("severe storms"))
            return HazardEventType.STORM;
        if (string.toLowerCase(Locale.ROOT).equals("volcanoes"))
            return HazardEventType.VOLCANO;
        if (string.toLowerCase(Locale.ROOT).equals("wildfires"))
            return HazardEventType.WILDFIRE;
        return HazardEventType.UNKNOWN;
    }
}
