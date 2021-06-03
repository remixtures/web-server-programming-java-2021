package weatherservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LocationController {

    @Autowired
    private LocationService locationService;


    @GetMapping("/locations")
    public String list(Model model) {
model.addAttribute("locations", locationService.getLocations());
        return "locations";
    }

    @GetMapping("/locations/{id}")
    public String view(Model model, @PathVariable Long id) {
model.addAttribute("location", locationService.getLocation(id));
        return "location";
    }

    @PostMapping("/locations")
    public String add(@ModelAttribute Location location) {
        locationService.addLocation(location);
        return "redirect:/locations";
    }
    
    @GetMapping("/flushcaches")
    public String clearCache() {
        locationService.flushCache();
        return "redirect:/";
    }
}
