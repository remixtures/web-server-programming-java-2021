package scoreservice;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByGame(Game game);

    Score findByGameAndId(Game game, Long id);
}
