package scoreservice;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Score extends AbstractPersistable<Long> {

    private String nickname;
    private Long points;
    @ManyToOne
    private Game game;

}
