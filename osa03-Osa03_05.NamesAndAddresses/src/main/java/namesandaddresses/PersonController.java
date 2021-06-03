package namesandaddresses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("persons", this.personRepository.findAll());
        return "index";
    }
}
