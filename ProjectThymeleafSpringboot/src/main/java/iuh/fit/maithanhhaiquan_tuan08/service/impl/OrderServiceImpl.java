package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.CartItem;
import iuh.fit.maithanhhaiquan_tuan08.entity.Customer;
import iuh.fit.maithanhhaiquan_tuan08.entity.Order;
import iuh.fit.maithanhhaiquan_tuan08.entity.OrderLine;
import iuh.fit.maithanhhaiquan_tuan08.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements iuh.fit.maithanhhaiquan_tuan08.service.OrderService {
    private final OrderRepository orderRepository;
    private final CustomerServiceImpl customerService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerServiceImpl customerService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> search(String keyword) {
        // Tìm kiếm theo tên khách hàng
        List<Order> orders = orderRepository.searchOrdersByCustomerName(keyword);

        // Nếu keyword là ngày tháng, tìm kiếm thêm theo ngày
        List<Order> ordersByDate = searchByDate(keyword);

        // Merge kết quả
        orders.addAll(ordersByDate.stream()
                .filter(o -> !orders.contains(o))
                .collect(Collectors.toList()));

        return orders;
    }

    private List<Order> searchByDate(String keyword) {
        try {
            LocalDate date = parseDate(keyword);
            if (date != null) {
                final LocalDate searchDate = date;
                return orderRepository.findAll().stream()
                        .filter(o -> o.getDate() != null && o.getDate().equals(searchDate))
                        .collect(Collectors.toList());
            }
        } catch (Exception ignored) {
        }

        return List.of();
    }

    private LocalDate parseDate(String keyword) {
        // Format: dd/MM/yyyy (20/10/2025)
        try {
            return LocalDate.parse(keyword, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            // Format: dd/MM (20/10) - sử dụng năm hiện tại
            try {
                String[] parts = keyword.split("/");
                if (parts.length == 2) {
                    return LocalDate.parse(parts[0] + "/" + parts[1] + "/" + LocalDate.now().getYear(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
            } catch (Exception ex) {
                // Format: yyyy-MM-dd (2025-10-20)
                try {
                    return LocalDate.parse(keyword, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public void checkout(List<CartItem> cart) {

        // 👇 Gán thẳng customer id = 1
        Customer customer = customerService.findById(1);
        System.out.println("khách hàng tên là " + customer);
        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setCustomer(customer);

        Set<OrderLine> lines = new HashSet<>();

        for (CartItem item : cart) {
            OrderLine line = new OrderLine();
            line.setOrder(order);
            line.setProduct(item.getProduct());
            line.setAmount(item.getQuantity());
            line.setPurchasePrice(item.getPriceAtAdd());
            lines.add(line);
        }

        order.setOrderLines(lines);
        orderRepository.save(order); // 👈 Lưu xuống DB
    }

    @Override
    public List<Order> findByCustomer(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

}
