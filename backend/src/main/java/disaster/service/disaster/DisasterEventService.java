package disaster.service.disaster;

import disaster.model.notification.NotificationUpdateReason;
import disaster.model.common.TimeSearchBounds;
import disaster.model.disaster.DisasterEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DisasterEventService {
    Flux<NotificationUpdateReason> getRepositoryUpdateNotifier();

    Flux<DisasterEvent> getDisasterEventsByBounds(TimeSearchBounds bounds);

    Mono<Void> initDisasterEventsUpdate();
}
