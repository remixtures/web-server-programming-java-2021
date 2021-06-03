package airports;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> list() {
        return airportRepository.findAll();
    }

    public void create(String identifier, String name) {
        Airport a = new Airport();
        a.setIdentifier(identifier);
        a.setName(name);
        List<Airport> airports = list();
        if (airports
                .stream()
                .filter(airport -> airport.getIdentifier().equals(identifier) 
                        || airport.getName().equals(name))
                .count() == 0) {
                        airportRepository.save(a);
        }
    }
}

