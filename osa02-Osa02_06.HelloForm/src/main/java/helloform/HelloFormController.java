package helloform;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloFormController {

    private String content = "Hello world!";

    @GetMapping("/")
    public String get(Model model) {
        model.addAttribute("content", content);
        return "index";
    }

    @PostMapping("/")
    public String post(@RequestParam String content) {
        this.content = content;
        // opimme tämän "redirect:/"-loitsun merkityksen ihan kohta!
        return "redirect:/";
    }
}
