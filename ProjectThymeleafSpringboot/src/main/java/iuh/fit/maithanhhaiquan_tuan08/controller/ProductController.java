package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import iuh.fit.maithanhhaiquan_tuan08.service.CommentService;
import iuh.fit.maithanhhaiquan_tuan08.service.ProductService;
import iuh.fit.maithanhhaiquan_tuan08.entity.Category;
import iuh.fit.maithanhhaiquan_tuan08.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    // Provide categories to product list/search views so frontend can show category
    // dropdown
    @ModelAttribute("categories")
    public java.util.List<Category> populateCategories() {
        return categoryService.getAll();
    }

    @GetMapping
    public String showAllProducts(Model model) {
        List<Product> list = productService.getAll();
        model.addAttribute("products", list);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("comments", commentService.getCommentsByProductId(id));
        return "product/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult, Model model) {
        // Nếu có lỗi validation, quay lại form và hiển thị lỗi
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/form";
        }

        productService.save(product);
        return "redirect:/product";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return "redirect:/product";
    }

    // ========== SEARCH & FILTER ENDPOINTS ==========

    /**
     * Tìm kiếm theo tên sản phẩm
     */
    @GetMapping("/search/name")
    public String searchByName(@RequestParam String name, Model model) {
        List<Product> products = productService.searchByName(name);
        model.addAttribute("products", products);
        model.addAttribute("searchName", name);
        model.addAttribute("searchType", "Tìm kiếm: " + name);
        return "product/list";
    }

    /**
     * Lọc theo danh mục
     */
    @GetMapping("/filter/category")
    public String filterByCategory(@RequestParam Integer categoryId, Model model) {
        List<Product> products = productService.searchByCategory(categoryId);
        model.addAttribute("products", products);
        model.addAttribute("filterCategoryId", categoryId);
        model.addAttribute("searchType", "Danh mục ID: " + categoryId);
        return "product/list";
    }

    /**
     * Lọc theo trạng thái tồn kho
     */
    @GetMapping("/filter/stock")
    public String filterByStock(@RequestParam boolean inStock, Model model) {
        List<Product> products = productService.searchByInStock(inStock);
        model.addAttribute("products", products);
        model.addAttribute("filterStock", inStock);
        model.addAttribute("searchType", inStock ? "Sản phẩm còn hàng" : "Sản phẩm hết hàng");
        return "product/list";
    }

    /**
     * Lọc theo khoảng giá
     */
    @GetMapping("/filter/price")
    public String filterByPrice(@RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Model model) {
        List<Product> products = productService.searchByPriceRange(minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("filterMinPrice", minPrice);
        model.addAttribute("filterMaxPrice", maxPrice);
        model.addAttribute("searchType", "Giá từ " + minPrice + " đến " + maxPrice);
        return "product/list";
    }

    /**
     * Tìm kiếm nâng cao: danh mục + tên
     */
    @GetMapping("/search/category-name")
    public String searchByCategoryAndName(@RequestParam Integer categoryId,
            @RequestParam String name,
            Model model) {
        List<Product> products = productService.searchByCategoryAndName(categoryId, name);
        model.addAttribute("products", products);
        model.addAttribute("searchName", name);
        model.addAttribute("searchCategoryId", categoryId);
        model.addAttribute("searchType", "Danh mục + Tên: " + name);
        return "product/list";
    }

    /**
     * Tìm kiếm nâng cao: tên + khoảng giá
     */
    @GetMapping("/search/name-price")
    public String searchByNameAndPrice(@RequestParam String name,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Model model) {
        List<Product> products = productService.searchByNameAndPrice(name, minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("searchName", name);
        model.addAttribute("filterMinPrice", minPrice);
        model.addAttribute("filterMaxPrice", maxPrice);
        model.addAttribute("searchType", "Tên + Giá: " + name + " (" + minPrice + "-" + maxPrice + ")");
        return "product/list";
    }

    /**
     * Tìm kiếm nâng cao: danh mục + khoảng giá
     */
    @GetMapping("/search/category-price")
    public String searchByCategoryAndPrice(@RequestParam Integer categoryId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Model model) {
        List<Product> products = productService.searchByCategoryAndPrice(categoryId, minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("searchCategoryId", categoryId);
        model.addAttribute("filterMinPrice", minPrice);
        model.addAttribute("filterMaxPrice", maxPrice);
        model.addAttribute("searchType", "Danh mục + Giá (" + minPrice + "-" + maxPrice + ")");
        return "product/list";
    }

    /**
     * Tìm kiếm toàn bộ nâng cao: tên + danh mục + giá
     */
    @GetMapping("/search/advanced")
    public String advancedSearch(@RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {
        List<Product> products = productService.searchAdvanced(name, categoryId, minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("searchName", name);
        model.addAttribute("searchCategoryId", categoryId);
        model.addAttribute("filterMinPrice", minPrice);
        model.addAttribute("filterMaxPrice", maxPrice);
        model.addAttribute("searchType", "Tìm kiếm nâng cao");
        return "product/list";
    }
}
