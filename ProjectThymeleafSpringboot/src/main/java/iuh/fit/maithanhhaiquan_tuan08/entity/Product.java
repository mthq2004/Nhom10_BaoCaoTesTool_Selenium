package iuh.fit.maithanhhaiquan_tuan08.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@ToString
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 2, max = 255, message = "Tên phải có ít nhất 2 ký tự và tối đa 255 ký tự")
    @Pattern(regexp = "^[A-Z].*", message = "Tên sản phẩm phải bắt đầu bằng chữ hoa")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.1", message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    private boolean inStock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
