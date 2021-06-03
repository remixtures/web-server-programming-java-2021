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
public class Actor extends AbstractPersistable<Long> {

    @Column
    private String name;
    @ManyToMany(mappedBy = "actors")
    private List<Movie> movies = new ArrayList<>();
}
