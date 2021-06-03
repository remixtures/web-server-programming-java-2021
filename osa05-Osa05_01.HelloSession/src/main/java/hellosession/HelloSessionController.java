package hellosession;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloSessionController {
    
    @RequestMapping("*")
    @ResponseBody
    public String sayHello(HttpSession session) {
        int visits= 1;
        if (session.getAttribute("count") != null) {
            return "Hello again!";
        } 
        
        session.setAttribute("count", visits);
        return "Hello there!";
       
    }
}
