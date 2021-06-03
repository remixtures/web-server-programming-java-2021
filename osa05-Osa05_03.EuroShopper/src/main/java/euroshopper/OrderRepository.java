package euroshopper;

import org.springframework.data.jpa.repository.JpaRepository;
import euroshopper.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
