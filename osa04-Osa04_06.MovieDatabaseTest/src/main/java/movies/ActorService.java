package movies;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieRepository movieRepository;

    public List<Actor> list() {
        return actorRepository.findAll();
    }

    @Transactional
    public void add(String name) {
        Actor actor = new Actor();
        actor.setName(name);

        actorRepository.save(actor);

    }

    @Transactional
    public void remove(Long actorId) {
        Actor actor = actorRepository.getOne(actorId);
        for (Movie movie : actor.getMovies()) {
            movie.getActors().remove(actor);
        }

        actorRepository.deleteById(actorId);
    }

    @Transactional
    public void addActorToMovie(Long actorId, Long movieId) {
        movieRepository.getOne(movieId).getActors().add(actorRepository.getOne(actorId));
    }

    public Actor findById(Long actorId) {
        return actorRepository.getOne(actorId);
    }
}
