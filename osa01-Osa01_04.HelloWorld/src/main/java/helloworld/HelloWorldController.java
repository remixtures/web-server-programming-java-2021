package helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorldController {
    
    @GetMapping("*")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }
}
