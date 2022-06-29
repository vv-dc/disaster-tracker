package disaster.service.stats;

import disaster.model.stats.FrequencyStatsEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FrequencyStatsService {

    Mono<Void> initFrequencyStatsUpdate();

    Flux<FrequencyStatsEntry> getFrequencyStatsEntries();

    Flux<FrequencyStatsEntry> getHottestFrequencyStats(int limit);
}
