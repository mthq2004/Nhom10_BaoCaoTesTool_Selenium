package iuh.fit.maithanhhaiquan_tuan08.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Trả về file templates/login.html
    }
}
