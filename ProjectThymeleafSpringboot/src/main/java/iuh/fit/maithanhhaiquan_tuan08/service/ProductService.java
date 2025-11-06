package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Product findById(Integer id);

    void save(Product product);

    void deleteById(Integer id);

    // ========== SEARCH & FILTER METHODS ==========

    /**
     * Tìm kiếm theo tên sản phẩm
     */
    List<Product> searchByName(String name);

    /**
     * Lọc theo danh mục
     */
    List<Product> searchByCategory(Integer categoryId);

    /**
     * Lọc theo trạng thái tồn kho
     */
    List<Product> searchByInStock(boolean inStock);

    /**
     * Lọc theo khoảng giá
     */
    List<Product> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm kiếm nâng cao: danh mục + tên
     */
    List<Product> searchByCategoryAndName(Integer categoryId, String name);

    /**
     * Tìm kiếm nâng cao: tên + khoảng giá
     */
    List<Product> searchByNameAndPrice(String name, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm kiếm nâng cao: danh mục + khoảng giá
     */
    List<Product> searchByCategoryAndPrice(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm kiếm toàn bộ nâng cao: tên + danh mục + giá
     */
    List<Product> searchAdvanced(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice);
}