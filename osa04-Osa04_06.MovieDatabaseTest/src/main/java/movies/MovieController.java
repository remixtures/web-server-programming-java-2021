package movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public String list(Model model) {
        model.addAttribute("movies", movieService.list());
        return "movies";
    }

    @PostMapping("/movies")
    public String add(@RequestParam String name, @RequestParam Integer lengthInMinutes) {
        movieService.add(name, lengthInMinutes);
        return "redirect:/movies";
    }

    @DeleteMapping("/movies/{movieId}")
    public String add(@PathVariable(value = "movieId") Long movieId) {
        movieService.remove(movieId);
        return "redirect:/movies";
    }
}
