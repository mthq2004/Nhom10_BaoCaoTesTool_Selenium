package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.OrderLine;

import java.util.List;

public interface OrderLineService {
    List<OrderLine> getAll();

    List<OrderLine> getByOrderId(Integer orderId);

    OrderLine findById(Integer id);

    void save(OrderLine orderLine);

    void delete(Integer id);
}
