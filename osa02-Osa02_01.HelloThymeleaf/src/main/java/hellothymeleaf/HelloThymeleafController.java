package hellothymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloThymeleafController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/video")
    public String video() {
        return "video";
    }
    
}
