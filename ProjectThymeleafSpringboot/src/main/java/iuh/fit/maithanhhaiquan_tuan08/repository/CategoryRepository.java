package iuh.fit.maithanhhaiquan_tuan08.repository;

import iuh.fit.maithanhhaiquan_tuan08.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Lấy category kèm products bằng join fetch để tránh
    // LazyInitializationException khi render view
    @Query("select c from Category c left join fetch c.products where c.id = :id")
    Optional<Category> findByIdWithProducts(@Param("id") Integer id);

}
