package playlists;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TrackController {

    @Autowired
    TrackRepository trackRepository;

    @GetMapping("/tracks")
    public String list(Model model) {
        model.addAttribute("tracks", trackRepository.findAll());
        return "tracks";
    }

    @PostMapping("/tracks")
    public String add(@RequestParam String band, @RequestParam String title, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate released, @RequestParam Integer durationInSeconds) {
        trackRepository.save(new Track(band, title, released, durationInSeconds));
        return "redirect:/tracks";
    }
}
