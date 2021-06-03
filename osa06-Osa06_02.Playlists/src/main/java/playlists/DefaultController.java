package playlists;

import org.springframework.web.bind.annotation.GetMapping;

public class DefaultController {

    @GetMapping("*")
    public String home() {
        return "index";
    }
}
