package iuh.fit.maithanhhaiquan_tuan08.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/")
public class HomeController {

    public HomeController() {
        super();
    }

    @GetMapping
    public String HomePage(Model model) {
        LocalDate date = LocalDate.now();
        String mess = "Welcome Thymeleaf";

        model.addAttribute("message", mess);
        model.addAttribute("date", date.toString());

        // Check if user is authenticated and has ADMIN role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                return "home"; // Show management dashboard for ADMIN
            }
        }

        // For customers and non-authenticated users, redirect to product list
        return "redirect:/product";
    }

    @GetMapping("/home")
    public String HomeNested(Model model) {
        return HomePage(model);
    }
}
