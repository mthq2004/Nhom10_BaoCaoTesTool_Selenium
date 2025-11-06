package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.OrderLine;
import iuh.fit.maithanhhaiquan_tuan08.service.OrderLineService;
import iuh.fit.maithanhhaiquan_tuan08.service.OrderService;
import iuh.fit.maithanhhaiquan_tuan08.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orderline")
public class OrderLineController {

    @Autowired
    private OrderLineService orderLineService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orderLines", orderLineService.getAll());
        return "orderline/list";
    }

}
