package iuh.fit.maithanhhaiquan_tuan08.util;

import iuh.fit.maithanhhaiquan_tuan08.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartUtil {

    public BigDecimal getTotal(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }
}
