package registration;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @ModelAttribute
    private Registration getRegistration() {
        return new Registration();
    }

    @GetMapping("/registrations")
    public String view() {
        return "form";
    }

    @PostMapping("/registrations")
    public String register(
            @Valid @ModelAttribute Registration registration,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "form";
        }
        
        registrationRepository.save(registration);
        return "redirect:/success";
    }

}
