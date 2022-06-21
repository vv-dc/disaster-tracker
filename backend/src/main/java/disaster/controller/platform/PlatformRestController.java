package disaster.controller.platform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PlatformRestController {

    @GetMapping("/platform/ping")
    public Mono<String> pingRoute() {
        return Mono.just("pong");
    }
}
