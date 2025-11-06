package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.CartItem;
import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface CartService {
    List<CartItem> getCart(HttpSession session);

    void addToCart(Product product, HttpSession session);

    void removeItem(Integer productId, HttpSession session);

    void clearCart(HttpSession session);
}
