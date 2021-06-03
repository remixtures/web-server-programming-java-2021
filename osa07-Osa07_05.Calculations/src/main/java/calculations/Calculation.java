package calculations;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Calculation extends AbstractPersistable<Long> {

    private String content;
    private String status;
    private String calculationResult;

}
