package disaster.config;

import disaster.service.disaster.DisasterEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final DisasterEventService disasterEventService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        disasterEventService.initEventsUpdate().subscribe(); // update disasters in DB
        // TODO: add statistics
    }
}
