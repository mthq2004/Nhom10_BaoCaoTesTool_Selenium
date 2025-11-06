package iuh.fit.maithanhhaiquan_tuan08.repository;

import iuh.fit.maithanhhaiquan_tuan08.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findByOrder_Id(Integer orderId);
}
