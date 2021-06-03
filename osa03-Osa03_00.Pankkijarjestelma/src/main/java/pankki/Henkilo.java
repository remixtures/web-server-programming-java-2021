package pankki;

import java.util.ArrayList;
import java.util.List;
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
public class Henkilo extends AbstractPersistable<Long> {

    private String nimi;
    @ManyToMany
    private List<Tili> tilit = new ArrayList<>();

}
