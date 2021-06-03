package form;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormController {

    @GetMapping("*")
    public String read() {
        return "index";
    }

    @GetMapping("/table1")
    public String table1() {
        return "table1";
    }

    @GetMapping("/table2")
    public String table2() {
        return "table2";
    }

    @GetMapping("/form1")
    public String form1() {
        return "form1";
    }

    @GetMapping("/form2")
    public String form2() {
        return "form2";
    }

    @PostMapping("/form")
    public String submitForm() {
        return "redirect:/index";
    }
}
