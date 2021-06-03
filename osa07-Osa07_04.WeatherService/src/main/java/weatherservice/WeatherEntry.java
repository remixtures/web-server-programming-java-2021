package weatherservice;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WeatherEntry extends AbstractPersistable<Long> {

    @ManyToOne
    private Location location;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date targetDate;

    private Integer degrees;
    private Integer chanceOfRain;

}
