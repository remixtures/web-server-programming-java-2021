package simplebanking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // tutustumme näihin kohta
    Account findByIban(String iban);
}
