package iuh.fit.maithanhhaiquan_tuan08.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
@ToString(exclude = "orders")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải có ít nhất 2 ký tự và tối đa 100 ký tự")
    @Pattern(regexp = "^[A-Z].*", message = "Tên khách hàng phải bắt đầu bằng chữ hoa")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate customerSince;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

}