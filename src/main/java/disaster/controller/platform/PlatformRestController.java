package disaster.controller.platform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlatformRestController {

    @GetMapping("/platform/ping")
    @ResponseBody
    public String pingRoute() {
        return "pong";
    }
}
