package hellopath;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloPathController {

    @GetMapping("/path")
    @ResponseBody
    public String hellopath() {
        return "Oikein!";
    } 
}
