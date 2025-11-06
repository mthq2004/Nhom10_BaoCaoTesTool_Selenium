package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.CartItem;
import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements iuh.fit.maithanhhaiquan_tuan08.service.CartService {

    @Override
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if(cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("CART", cart);
        }
        return cart;
    }

    @Override
    public void addToCart(Product product, HttpSession session) {
        List<CartItem> cart = getCart(session);

        for(CartItem item : cart) {
            if(item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cart.add(new CartItem(product, 1, product.getPrice()));
    }

    @Override
    public void removeItem(Integer productId, HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(p -> p.getProduct().getId().equals(productId));
    }

    @Override
    public void clearCart(HttpSession session) {
        session.removeAttribute("CART");
    }
}
