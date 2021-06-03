package movies;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;

    public Iterable<Movie> list() {
        return movieRepository.findAll();
    }

    @Transactional
    public void add(String name, int lengthInMinutes) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setLengthInMinutes(lengthInMinutes);
        movieRepository.save(movie);
    }

    @Transactional
    public void remove(Long movieId) {
        Movie movie = movieRepository.getOne(movieId);
        for (Actor actor : movie.getActors()) {
            actor.getMovies().remove(movie);
        }

        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public List<Movie> listMoviesWithout(Long actorId) {
        Actor actor = actorRepository.getOne(actorId);
        return movieRepository.findMoviesWithoutActor(actor);
    }
}
