/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scoreservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author miguel
 */

@RestController
public class GameController {
    
    @Autowired
    private GameRepository gameRepository;
    
    @PostMapping("/games")
    public Game createGame(@RequestBody Game game) {
        return gameRepository.save(game);
    }
    
    @GetMapping("/games")
    public List<Game> getGames() {
        return gameRepository.findAll();
    }
    
    @GetMapping("/games/{name}")
    public Game getName(@PathVariable String name) {
        return gameRepository.findByName(name);
    }
    
    @DeleteMapping("/games/{name}")
    public Game deleteGame(@PathVariable String name) {
        Game game = gameRepository.findByName(name);
        gameRepository.delete(game);
        return game;
    }
}
