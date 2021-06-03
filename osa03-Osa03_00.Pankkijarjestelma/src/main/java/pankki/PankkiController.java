package pankki;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PankkiController {

    @Autowired
    private PankkiRepository pankkiRepository;
    @Autowired
    private KonttoriRepository konttoriRepository;

    @GetMapping("/pankit")
    public String list(Model model, @RequestParam(defaultValue = "0") Integer sivu) {
        Pageable pageable = PageRequest.of(sivu, 10, Sort.by("nimi").descending());
        model.addAttribute("pankit", pankkiRepository.findAll(pageable));
        return "pankit";
    }

    @PostMapping("/pankit")
    public String create(@RequestParam String nimi) {
        pankkiRepository.save(new Pankki(nimi, new ArrayList<>(), new ArrayList<>()));
        return "redirect:/pankit";
    }

    @GetMapping("/pankit/{id}")
    public String getOne(Model model, @PathVariable Long id) {
        model.addAttribute("pankki", pankkiRepository.getOne(id));
        return "pankki";
    }

    @PostMapping("/pankit/{id}/konttorit")
    public String addKonttori(@PathVariable Long id, @RequestParam String osoite) {
        Pankki p = pankkiRepository.getOne(id);
        Konttori k = new Konttori(osoite, p);
        konttoriRepository.save(k);
        return "redirect:/pankit/" + id;
    }
}
