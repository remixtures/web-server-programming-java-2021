package euroshopper;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class OrderItem extends AbstractPersistable<Long> {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;
    @Min(1)
    private Long itemCount;

}
