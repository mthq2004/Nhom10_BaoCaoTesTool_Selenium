package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.Category;
import iuh.fit.maithanhhaiquan_tuan08.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "category/list";
    }

    /**
     * Xem chi tiết một danh mục và danh sách sản phẩm thuộc danh mục đó
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return "redirect:/category";
        }
        model.addAttribute("category", category);
        // products sẽ được lấy từ category.getProducts()
        model.addAttribute("products", category.getProducts());
        return "category/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category category, BindingResult bindingResult) {
        // Nếu có lỗi validation, quay lại form và hiển thị lỗi
        if (bindingResult.hasErrors()) {
            return "category/form";
        }

        categoryService.save(category);
        return "redirect:/category";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return "redirect:/category";
    }
}
