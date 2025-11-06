package iuh.fit.maithanhhaiquan_tuan08.entity;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartItem {
    private Product product;
    private int quantity;
    private BigDecimal priceAtAdd;

    public BigDecimal getTotal() {
        return priceAtAdd.multiply(BigDecimal.valueOf(quantity));
    }
}
