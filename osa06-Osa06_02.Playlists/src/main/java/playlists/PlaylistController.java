package playlists;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    TrackRepository trackRepository;

    @GetMapping("/playlists")
    public String list(Model model) {
        model.addAttribute("playlists", playlistRepository.findAll());
        model.addAttribute("tracks", trackRepository.findAll());
        return "playlists";
    }

    @PostMapping("/playlists")
    public String add(String name) {
        playlistRepository.save(new Playlist(name));
        return "redirect:/playlists";
    }

    @Transactional
    @PostMapping("/playlists/{playlistId}/tracks")
    public String addToPlaylist(@PathVariable Long playlistId, @RequestParam Long trackId) {
        playlistRepository.getOne(playlistId).getTracks().add(trackRepository.getOne(trackId));
        return "redirect:/playlists";
    }
}
