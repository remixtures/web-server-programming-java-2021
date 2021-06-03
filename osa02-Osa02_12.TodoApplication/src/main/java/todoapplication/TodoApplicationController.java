package todoapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoApplicationController {

    @Autowired
    private ItemRepository itemRepository;
    
    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("items", this.itemRepository.findAll());
        return "index";
    }
    
    @PostMapping("/")
    public String add(@RequestParam String name) {
        itemRepository.save(new Item(name, 0));
        return "redirect:/";
    }
    
    @GetMapping("/{id}")
    public String getItem(Model model, @PathVariable Long id) {
        Item item = this.itemRepository.getOne(id);
        item.setChecked(item.getChecked() + 1);
        this.itemRepository.save(item);
        model.addAttribute("item", item);
        return "todo";
    }
}
