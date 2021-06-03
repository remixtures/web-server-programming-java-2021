package helloauthentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("*")
    public String handleDefault() {
        return "redirect:/messages";
    }
}
