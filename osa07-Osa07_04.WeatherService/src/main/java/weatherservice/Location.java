package weatherservice;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location extends AbstractPersistable<Long> {

    private String name;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private List<WeatherEntry> weatherEntries;


}
