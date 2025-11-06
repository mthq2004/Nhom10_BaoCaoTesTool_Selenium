package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.CartItem;
import iuh.fit.maithanhhaiquan_tuan08.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    Order getById(Integer id);

    Order save(Order order);

    void delete(Integer id);

    List<Order> search(String keyword);

    void checkout(List<CartItem> cart);

    List<Order> findByCustomer(Integer customerId);
}