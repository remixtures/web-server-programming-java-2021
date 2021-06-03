package hiddenfields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/messages")
    public String view(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "messages";
    }
    
    @Secured("POSTER")
    @PostMapping("/messages")
    public String add(@ModelAttribute Message message) {
        if (message.getContent() != null && !message.getContent().isEmpty()) {
            messageRepository.save(message);
        }

        return "redirect:/messages";
    }
}
