package iuh.fit.maithanhhaiquan_tuan08.repository;

import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
