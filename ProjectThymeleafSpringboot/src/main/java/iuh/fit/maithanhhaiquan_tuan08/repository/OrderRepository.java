package iuh.fit.maithanhhaiquan_tuan08.repository;

import iuh.fit.maithanhhaiquan_tuan08.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE LOWER(o.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Order> searchOrdersByCustomerName(@Param("keyword") String keyword);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findByCustomerId(Integer customerId);

}
