package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.Comment;
import iuh.fit.maithanhhaiquan_tuan08.entity.Product;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);

    void deleteById(Integer id);

    Comment findById(Integer id);

    List<Comment> getAll();

    List<Comment> getCommentsByProduct(Product product);

    List<Comment> getCommentsByProductId(Integer productId);
}
