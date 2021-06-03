package euroshopper;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import euroshopper.Item;
import euroshopper.ItemRepository;

@Controller
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @PostConstruct
    public void init() {
        // add some content to the product repository
        itemRepository.save(new Item("Pee Cola", 2.0));
        itemRepository.save(new Item("Clay Modeling with Pooh", 10.0));
        itemRepository.save(new Item("Poke-a-Bone", 12.0));
        itemRepository.save(new Item("Bishops Finger", 5.0));
    }

    @GetMapping("/items")
    public String list(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "items";
    }
}
