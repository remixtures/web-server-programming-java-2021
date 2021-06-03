package hellolist;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloListController {

    private List<String> list;

    public HelloListController() {
        this.list = new ArrayList<>();
        this.list.add("Hello world!");
        this.list.add("+[-[<<[+[--->]-[<<<]]]>>>-]>-.---.>..>.<<<<-.<+.>>>>>.>.<<.<-.");
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("list", list);
        return "index";
    }

    // Älä koske tähän metodiin -- tutustumme tiedon lisäämiseen hieman myöhemmin.
    @PostMapping("/")
    public String addContent(@RequestParam String content) {
        this.list.add(content.trim());
        return "redirect:/";
    }

}
