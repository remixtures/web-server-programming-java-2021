package playlists;

import java.time.LocalDate;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Track extends AbstractPersistable<Long> {

    String band;
    String title;
    LocalDate released;
    Integer durationInSeconds;
}
