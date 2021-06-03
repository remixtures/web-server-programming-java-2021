package hellofragments;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloFragmentsController {

    @GetMapping("/index")
    public String read() {
        return "index";
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }
}
