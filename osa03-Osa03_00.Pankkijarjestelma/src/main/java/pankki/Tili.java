package pankki;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NamedEntityGraph(name = "Tili.omistajatJaPankit",
        attributeNodes = {
            @NamedAttributeNode("omistajat"),
            @NamedAttributeNode("pankki")})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tili extends AbstractPersistable<Long> {

    private BigDecimal saldo = new BigDecimal(0);

    @ManyToOne
    private Pankki pankki;

    @ManyToMany(mappedBy = "tilit")
    private List<Henkilo> omistajat = new ArrayList<>();

}
