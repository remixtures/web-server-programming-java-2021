package itemdatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemDatabaseController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("items", this.itemRepository.findAll());
        return "index";
    }
    
    @PostMapping("/")
    public String add(@RequestParam String name) {
        itemRepository.save(new Item(name));
        return "redirect:/";
    }

}
