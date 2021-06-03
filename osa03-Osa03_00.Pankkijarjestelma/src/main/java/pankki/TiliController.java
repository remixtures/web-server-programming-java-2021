package pankki;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TiliController {

    @Autowired
    private TiliRepository tiliRepository;

    @Autowired
    private HenkiloRepository henkiloRepository;

    @Autowired
    private PankkiRepository pankkiRepository;

    @GetMapping("/tilit")
    public String list(Model model) {
        model.addAttribute("tilit", tiliRepository.findByIdNotNull());
        model.addAttribute("pankit", pankkiRepository.findAll());
        return "tilit";
    }

    @PostMapping("/tilit")
    public String create(@RequestParam Long pankkiId) {
        Pankki p = pankkiRepository.getOne(pankkiId);
        Tili t = new Tili(new BigDecimal(0), p, new ArrayList<>());
        tiliRepository.save(t);
        return "redirect:/tilit";
    }

    @GetMapping("/tilit/{id}")
    public String getOne(Model model, @PathVariable Long id) {
        Tili tili = tiliRepository.getOne(id);
        List<Henkilo> henkilot = henkiloRepository.findAll();
        henkilot.removeAll(tili.getOmistajat());

        model.addAttribute("tili", tili);
        model.addAttribute("tilit", tiliRepository.findAll());
        model.addAttribute("henkilot", henkilot);

        return "tili";
    }

    @PostMapping("/tilit/{mistaId}/siirra")
    public String siirra(@PathVariable Long mistaId, 
            @RequestParam Long minneId, 
            @RequestParam BigDecimal summa) {
        Tili mista = tiliRepository.getOne(mistaId);
        Tili minne = tiliRepository.getOne(minneId);
        
        mista.setSaldo(mista.getSaldo().subtract(summa));
        minne.setSaldo(minne.getSaldo().add(summa));
        
        tiliRepository.save(mista);
        tiliRepository.save(minne);

        return "redirect:/tilit/" + mistaId;
    }

    @Transactional
    @PostMapping("/tilit/{tiliId}/omistajat/{henkiloId}")
    public String addOmistaja(@PathVariable Long tiliId, @PathVariable Long henkiloId) {
        henkiloRepository
                .getOne(henkiloId)
                .getTilit().add(tiliRepository.getOne(tiliId));

        return "redirect:/tilit/" + tiliId;
    }

    @GetMapping("/henkiloita")
    public String lisaaHenkiloita() {
        henkiloRepository.save(new Henkilo("Tupu", new ArrayList<>()));
        henkiloRepository.save(new Henkilo("Hupu", new ArrayList<>()));
        henkiloRepository.save(new Henkilo("Lupu", new ArrayList<>()));
        return "redirect:/tilit";
    }
}
