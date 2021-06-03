package reservations;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ReservationTestUtilities {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    public PasswordEncoder passwordEncoder;

    public long reservationCount() {
        return reservationRepository.count();
    }

    public void clearReservations() {
        reservationRepository.deleteAll();
    }

    public void addReservation(String username, LocalDate from, LocalDate to) {

        Account a = accountRepository.findByUsername(username);
        if (a == null) {
            a = new Account();
            a.setUsername(username);
            a.setPassword(passwordEncoder.encode("pw"));

            a = accountRepository.save(a);
        }

        Reservation r = new Reservation();
        r.setUser(a);
        r.setReservationFrom(from);
        r.setReservationTo(to);

        reservationRepository.save(r);
    }
}
