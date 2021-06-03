package greeting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    @GetMapping("/greet")
    @ResponseBody
    public String greeting(@RequestParam String name, @RequestParam String greeting) {
        return greeting + ", " + name;
    }
}
