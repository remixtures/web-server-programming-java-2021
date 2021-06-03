package pankki;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TiliRepository extends JpaRepository<Tili, Long> {

    @EntityGraph(value = "Tili.omistajatJaPankit")
    List<Tili> findByIdNotNull();
}
