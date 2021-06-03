package movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActorController {

    @Autowired
    private ActorService actorService;
    @Autowired
    private MovieService movieService;

    @GetMapping("/actors")
    public String list(Model model) {
        model.addAttribute("actors", actorService.list());
        return "actors";
    }

    @PostMapping("/actors")
    public String add(@RequestParam String name) {
        actorService.add(name);
        return "redirect:/actors";
    }

    @GetMapping("/actors/{actorId}")
    public String view(Model model, @PathVariable(value = "actorId") Long actorId) {
        model.addAttribute("actor", actorService.findById(actorId));
        model.addAttribute("movies", movieService.listMoviesWithout(actorId));
        return "actor";
    }

    @DeleteMapping("/actors/{actorId}")
    public String remove(@PathVariable(value = "actorId") Long actorId) {
        actorService.remove(actorId);
        return "redirect:/actors";
    }

    @PostMapping(value = "/actors/{actorId}/movies")
    public String addActorToMovie(@PathVariable(value = "actorId") Long actorId,
            @RequestParam(value = "movieId") Long movieId) {
        actorService.addActorToMovie(actorId, movieId);
        return "redirect:/actors";
    }
}
