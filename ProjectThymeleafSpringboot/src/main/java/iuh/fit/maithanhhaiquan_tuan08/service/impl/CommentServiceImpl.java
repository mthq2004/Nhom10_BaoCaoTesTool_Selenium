package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.Comment;
import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import iuh.fit.maithanhhaiquan_tuan08.repository.CommentRepository;
import iuh.fit.maithanhhaiquan_tuan08.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment findById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByProduct(Product product) {
        return commentRepository.findByProduct(product);
    }

    @Override
    public List<Comment> getCommentsByProductId(Integer productId) {
        return commentRepository.findByProductId(productId);
    }
}
