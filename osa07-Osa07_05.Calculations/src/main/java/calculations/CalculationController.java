package calculations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CalculationController {

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private CalculationService calculationService;

    @GetMapping("/calculations")
    public String view(Model model) {
        model.addAttribute("calculations", calculationRepository.findAll());
        return "calculations";
    }

    @GetMapping("/calculations/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("calculation", calculationRepository.getOne(id));
        return "calculation";
    }

    @PostMapping("/calculations")
    public String create(RedirectAttributes redirectAttributes,
            @ModelAttribute Calculation calculation) {
        calculation.setStatus("PROCESSING");
        calculationRepository.save(calculation); 
        calculationService.process(calculation);
        
        // käytännössä sama kuin 
        // return "redirect:/calculations/ + calculation.getId();
        
        redirectAttributes.addAttribute("id", calculation.getId());
        return "redirect:/calculations/{id}";
    }
}
