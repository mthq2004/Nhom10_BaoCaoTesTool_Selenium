package iuh.fit.maithanhhaiquan_tuan08.repository;

import iuh.fit.maithanhhaiquan_tuan08.entity.Comment;
import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByProduct(Product product);

    List<Comment> findByProductId(Integer productId);
}
