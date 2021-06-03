package movies;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends AbstractPersistable<Long> {

    @Column
    private String name;
    @Column
    private Integer lengthInMinutes;
    @ManyToMany
    private List<Actor> actors = new ArrayList<>();

}
