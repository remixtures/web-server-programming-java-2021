package registration;

import org.springframework.data.jpa.repository.JpaRepository;
import registration.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

}
