package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.Customer;
import iuh.fit.maithanhhaiquan_tuan08.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String list(Model model) {
        List<Customer> customers = customerService.getAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Customer> customers = customerService.search(keyword);
        model.addAttribute("customers", customers);
        model.addAttribute("keyword", keyword);
        return "customer/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult) {
        // Nếu có lỗi validation, quay lại form và hiển thị lỗi
        if (bindingResult.hasErrors()) {
            return "customer/form";
        }

        customerService.save(customer);
        return "redirect:/customer";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customer/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customer/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        customerService.deleteById(id);
        return "redirect:/customer";
    }
}