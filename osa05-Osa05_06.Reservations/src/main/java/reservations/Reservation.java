package reservations;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends AbstractPersistable<Long> {

    @ManyToOne
    private Account user;
    private LocalDate reservationFrom;
    private LocalDate reservationTo;

}
