package simplebanking;

import java.util.ArrayList;
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
public class Client extends AbstractPersistable<Long> {

    private String name;

    // DO SOMETHING HERE
    @OneToMany(mappedBy = "client")
    private List<Account> accounts = new ArrayList<>();
}
