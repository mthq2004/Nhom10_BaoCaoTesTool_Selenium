package iuh.fit.maithanhhaiquan_tuan08.repository;

import iuh.fit.maithanhhaiquan_tuan08.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %?1% OR c.name LIKE %?1%")
    List<Customer> search(String keyword);

    Customer findByName(String username);

}