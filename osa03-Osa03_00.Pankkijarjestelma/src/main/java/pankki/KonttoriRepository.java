package pankki;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KonttoriRepository extends JpaRepository<Konttori, Long> {

    List<Konttori> findByOsoite(String osoite);
    Konttori findByOsoiteAndPankki(String osoite, Pankki pankki);
}
