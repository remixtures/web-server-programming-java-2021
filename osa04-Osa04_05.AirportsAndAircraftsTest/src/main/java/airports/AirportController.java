package airports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping("/airports")
    public String list(Model model) {
        model.addAttribute("airports", airportService.list());
        return "airports";
    }

    @PostMapping("/airports")
    public String create(@RequestParam String identifier, @RequestParam String name) {
        airportService.create(identifier, name);
        return "redirect:/airports";
    }
}
