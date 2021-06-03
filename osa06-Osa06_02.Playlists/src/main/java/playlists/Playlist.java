package playlists;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Playlist extends AbstractPersistable<Long> {

    String name;
    @ManyToMany
    List<Track> tracks = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }
}
