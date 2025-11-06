package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import iuh.fit.maithanhhaiquan_tuan08.service.CartService;
import iuh.fit.maithanhhaiquan_tuan08.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Product product = productService.findById(productId);
        if (product != null) {
            cartService.addToCart(product, session);
            redirectAttributes.addFlashAttribute("successMessage",
                    "✅ Sản phẩm \"" + product.getName() + "\" đã được thêm vào giỏ hàng!");
        }
        return "redirect:/product";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCart(session));
        return "cart/list"; // file giỏ hàng của bạn đã có
    }

    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Integer id, HttpSession session) {
        cartService.removeItem(id, session);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}
