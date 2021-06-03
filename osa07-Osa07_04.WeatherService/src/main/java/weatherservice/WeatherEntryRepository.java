package weatherservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherEntryRepository extends JpaRepository<WeatherEntry, Long> {

}
