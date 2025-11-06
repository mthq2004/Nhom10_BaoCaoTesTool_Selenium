package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.Comment;
import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import iuh.fit.maithanhhaiquan_tuan08.service.CommentService;
import iuh.fit.maithanhhaiquan_tuan08.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addComment(@RequestParam Integer productId, @RequestParam String text) {
        Product product = productService.findById(productId);
        if (product != null && text != null && !text.trim().isEmpty()) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setProduct(product);
            commentService.save(comment);
        }
        return "redirect:/product/" + productId;
    }

    @GetMapping("/delete/{id}/{productId}")
    public String deleteComment(@PathVariable Integer id, @PathVariable Integer productId) {
        commentService.deleteById(id);
        return "redirect:/product/" + productId;
    }
}
