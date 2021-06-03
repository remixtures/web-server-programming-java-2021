package pankki;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Pankki extends AbstractPersistable<Long> {

    private String nimi;

    @OneToMany(mappedBy = "pankki")
    private List<Konttori> konttorit = new ArrayList<>();

    @OneToMany(mappedBy = "pankki")
    private List<Tili> tilit = new ArrayList<>();
}
