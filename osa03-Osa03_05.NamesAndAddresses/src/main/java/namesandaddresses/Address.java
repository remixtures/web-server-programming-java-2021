package namesandaddresses;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address extends AbstractPersistable<Long> {

    @OneToMany(mappedBy = "address")
    private List<Person> persons;

    private String street;

}
