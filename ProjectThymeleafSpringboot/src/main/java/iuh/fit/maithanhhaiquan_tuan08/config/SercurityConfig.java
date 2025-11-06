package iuh.fit.maithanhhaiquan_tuan08.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SercurityConfig {

    // ✅ Bộ mã hóa mật khẩu
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Tạo 2 tài khoản mẫu trong bộ nhớ
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails customer = User.builder()
                .username("customer")
                .password(passwordEncoder().encode("111"))
                .roles("CUSTOMER")
                .build();

        return new InMemoryUserDetailsManager(admin, customer);
    }

    // ✅ Cấu hình bảo mật chính
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 🔒 Phân quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll() // ai cũng truy cập
                                                                                                  // được
                        // Cho phép mọi người xem trang gốc, danh sách và chi tiết sản phẩm, và danh mục
                        // (GET)
                        .requestMatchers(HttpMethod.GET, "/", "/product/**", "/category/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // chỉ admin
                        // Các hành động liên quan tới giỏ hàng, đặt hàng, quản lý product
                        // (POST/PUT/DELETE) yêu cầu role
                        .requestMatchers("/cart/**", "/order/**", "/orderline/**").hasAnyRole("CUSTOMER", "ADMIN")
                        // AI chat APIs: cho phép mọi người truy cập
                        .requestMatchers("/ai-chat/**", "/api/ai/**").permitAll()
                        .anyRequest().authenticated() // các request khác phải đăng nhập
                )

                // 🧩 Cấu hình form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // Lấy role của user
                            var authorities = authentication.getAuthorities();
                            String role = authorities.iterator().next().getAuthority();

                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/"); // Admin → Trang chủ
                            } else {
                                response.sendRedirect("/product"); // Customer → thẳng vào product
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll())

                // 🚪 Cấu hình logout
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL đăng xuất
                        .logoutSuccessUrl("/login?logout=true") // sau khi logout thành công
                        .invalidateHttpSession(true) // hủy session hiện tại
                        .deleteCookies("JSESSIONID") // xóa cookie session
                        .permitAll() // cho phép ai cũng logout được
                )

                // 🛡️ Cấu hình CSRF: bỏ CSRF cho API chat
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/ai-chat/**", "/api/ai/**") // API không cần CSRF token
                );

        return http.build();
    }
}
