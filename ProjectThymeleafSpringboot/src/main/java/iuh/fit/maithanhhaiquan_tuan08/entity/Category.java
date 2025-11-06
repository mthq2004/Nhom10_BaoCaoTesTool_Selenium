package iuh.fit.maithanhhaiquan_tuan08.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@ToString
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 2, max = 100, message = "Tên danh mục phải có ít nhất 2 ký tự và tối đa 100 ký tự")
    @Pattern(regexp = "^[A-Z].*", message = "Tên danh mục phải bắt đầu bằng chữ hoa")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

}
