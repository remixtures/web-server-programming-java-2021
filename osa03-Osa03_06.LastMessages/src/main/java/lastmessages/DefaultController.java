package lastmessages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String redirectToMessages() {
        return "redirect:/messages";
    }
}
