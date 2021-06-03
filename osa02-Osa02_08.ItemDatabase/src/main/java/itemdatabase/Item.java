package itemdatabase;
 
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
 
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item extends AbstractPersistable<Long> {
 
    private String name;
 
}
