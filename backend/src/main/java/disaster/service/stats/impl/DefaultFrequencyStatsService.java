package disaster.service.stats.impl;

import disaster.dao.stats.FrequencyStatsDao;
import disaster.model.stats.FrequencyStatsEntry;
import disaster.module.mongo.FrequencyStats;
import disaster.service.stats.FrequencyStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class DefaultFrequencyStatsService implements FrequencyStatsService {

    private final FrequencyStatsDao frequencyStatsDao;

    public Flux<FrequencyStatsEntry> getFrequencyStatsEntries() {
        return frequencyStatsDao.getActiveFrequencyStats()
            .flatMapIterable(FrequencyStats::getItems);
    }

    public Flux<FrequencyStatsEntry> getHottestFrequencyStats(int limit) {
        return this.getFrequencyStatsEntries()
            .sort((a, b) -> b.getFrequency() - a.getFrequency())
            .take(limit);
    }

    public Mono<Void> initFrequencyStatsUpdate() {
        return Flux.interval(Duration.ZERO, Duration.ofHours(1))
            .concatMap((event) -> frequencyStatsDao.aggregateAllFrequencyStats())
            .then();
    }
}
