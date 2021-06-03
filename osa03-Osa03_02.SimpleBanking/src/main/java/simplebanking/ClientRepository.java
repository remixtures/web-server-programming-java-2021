package simplebanking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // tutustumme näihin kohta
    Client findByName(String name);
}
