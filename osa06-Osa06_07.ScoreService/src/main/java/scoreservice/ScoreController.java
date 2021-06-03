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
public class ScoreController {
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @PostMapping("/games/{name}/scores")
    public Score setGameScore(@PathVariable String name, @RequestBody Score score) {
        Game game =  gameRepository.findByName(name);
        score.setGame(game);
        return scoreRepository.save(score);
    }
    
    @GetMapping("/games/{name}/scores")
    public List<Score> getGameScores(@PathVariable String name) {
        Game game =  gameRepository.findByName(name);
        return scoreRepository.findByGame(game);
    }
    
    @GetMapping("/games/{name}/scores/{id}")
    public Score getGameScoresById(@PathVariable String name, @PathVariable Long id) {
        Game game =  gameRepository.findByName(name);
        return scoreRepository.findByGameAndId(game, id);
    }

    @DeleteMapping("/games/{name}/scores/{id}")
    public Score deleteGameScore(@PathVariable String name, @PathVariable Long id) {
        Game game =  gameRepository.findByName(name);
        Score score = scoreRepository.findByGameAndId(game, id);
        scoreRepository.delete(score);
        return score;
    }
}
