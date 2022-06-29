package disaster.model.stats;

import lombok.Data;

import java.util.List;

@Data
public class StatsRawEntry {

    private String id; // location
    private List<StatsRawTerm> terms;
}

