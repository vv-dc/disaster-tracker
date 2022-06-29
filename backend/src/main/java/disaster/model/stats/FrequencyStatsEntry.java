package disaster.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FrequencyStatsEntry {

    private String location;
    private int frequency;
}
