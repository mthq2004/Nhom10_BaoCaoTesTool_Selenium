package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.OrderLine;
import iuh.fit.maithanhhaiquan_tuan08.repository.OrderLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineServiceImpl implements iuh.fit.maithanhhaiquan_tuan08.service.OrderLineService {
    private final OrderLineRepository orderLineRepository;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public List<OrderLine> getAll() {
        return orderLineRepository.findAll();
    }

    @Override
    public List<OrderLine> getByOrderId(Integer orderId) {
        return orderLineRepository.findByOrder_Id(orderId);
    }

    @Override
    public OrderLine findById(Integer id) {
        return orderLineRepository.findById(id).orElse(null);
    }

    @Override
    public void save(OrderLine orderLine) {
        orderLineRepository.save(orderLine);
    }

    @Override
    public void delete(Integer id) {
        orderLineRepository.deleteById(id);
    }
}
