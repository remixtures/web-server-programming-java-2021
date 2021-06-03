package lastmessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/messages")
    public String list(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("messageDate").descending());
        model.addAttribute("messages", messageRepository.findAll(pageable));
        return "messages";
    }

    @PostMapping("/messages")
    public String create(@RequestParam String message) {
        Message msg = new Message();
        msg.setMessage(message);
        messageRepository.save(msg);

        return "redirect:/messages";
    }
}
