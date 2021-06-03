package htmlpractice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlPracticeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
