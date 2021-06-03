package onlyfortheselected;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecretDataController {

    @GetMapping("/happypath")
    @ResponseBody
    public String happy() {
        return "Happy!";
    }

    @GetMapping("/secretpath")
    @ResponseBody
    public String secret() {
        return "Secret!";
    }

    @GetMapping("/adminpath")
    @ResponseBody
    public String admin() {
        return "Admin!";
    }

}
