package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.CartItem;
import iuh.fit.maithanhhaiquan_tuan08.entity.Order;
import iuh.fit.maithanhhaiquan_tuan08.entity.OrderLine;
import iuh.fit.maithanhhaiquan_tuan08.service.CustomerService;
import iuh.fit.maithanhhaiquan_tuan08.service.OrderLineService;
import iuh.fit.maithanhhaiquan_tuan08.service.OrderService;
import iuh.fit.maithanhhaiquan_tuan08.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderLineService orderLineService;
    private final ProductService productService;

    public OrderController(OrderService orderService, CustomerService customerService,
            OrderLineService orderLineService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.orderLineService = orderLineService;
        this.productService = productService;
    }

    @GetMapping
    public String listOrders(Model model, Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // 👑 Admin thấy tất cả
            model.addAttribute("orders", orderService.getAll());
        } else {
            // 👤 Customer chỉ thấy đơn hàng của mình
            String username = authentication.getName();
            model.addAttribute("orders", orderService.findByCustomer(1));
        }
        return "order/list";
    }

    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("customers", customerService.getAll());
        return "order/form";
    }

    @PreAuthorize("hasRole('ADMIN')")

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order) {
        orderService.save(order);
        return "redirect:/order";
    }

    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Integer id, Model model) {
        model.addAttribute("order", orderService.getById(id));
        model.addAttribute("customers", customerService.getAll());
        return "order/form";
    }

    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
        return "redirect:/order";
    }

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable Integer id, Model model) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        model.addAttribute("orderLines", orderLineService.getByOrderId(id));
        model.addAttribute("products", productService.getAll());
        return "order/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}/orderline/add")
    public String addOrderLineForm(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getById(orderId);
        OrderLine orderLine = new OrderLine();
        orderLine.setOrder(order);
        model.addAttribute("orderLine", orderLine);
        model.addAttribute("order", order);
        model.addAttribute("products", productService.getAll());
        return "order/orderline-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{orderId}/orderline/save")
    public String saveOrderLine(@PathVariable Integer orderId, @ModelAttribute OrderLine orderLine) {
        if (orderLine.getId() == null) {
            Order order = orderService.getById(orderId);
            orderLine.setOrder(order);
        }
        orderLineService.save(orderLine);
        return "redirect:/order/" + orderId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}/orderline/edit/{lineId}")
    public String editOrderLineForm(@PathVariable Integer orderId, @PathVariable Integer lineId, Model model) {
        Order order = orderService.getById(orderId);
        OrderLine orderLine = orderLineService.findById(lineId);
        model.addAttribute("orderLine", orderLine);
        model.addAttribute("order", order);
        model.addAttribute("products", productService.getAll());
        return "order/orderline-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}/orderline/delete/{lineId}")
    public String deleteOrderLine(@PathVariable Integer orderId, @PathVariable Integer lineId) {
        orderLineService.delete(lineId);
        return "redirect:/order/" + orderId;
    }

    @GetMapping("/search")
    public String searchOrders(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("orders", orderService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("orders", orderService.getAll());
        }
        return "order/list";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        System.out.println("đã tới đấy");
        orderService.checkout(cart);

        session.removeAttribute("CART"); // ✅ Xóa giỏ sau khi đặt hàng

        return "redirect:/order/success";
    }

    @GetMapping("/success")
    public String success() {
        return "order/success";
    }

}