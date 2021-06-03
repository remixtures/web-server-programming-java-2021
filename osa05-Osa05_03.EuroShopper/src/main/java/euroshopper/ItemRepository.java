package euroshopper;

import org.springframework.data.jpa.repository.JpaRepository;
import euroshopper.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
