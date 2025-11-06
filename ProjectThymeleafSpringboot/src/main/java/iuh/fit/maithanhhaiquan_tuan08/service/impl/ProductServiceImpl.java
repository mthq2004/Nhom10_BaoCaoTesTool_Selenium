package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import iuh.fit.maithanhhaiquan_tuan08.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements iuh.fit.maithanhhaiquan_tuan08.service.ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(String.valueOf(id)).orElse(null);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(String.valueOf(id));
    }

    // ========== SEARCH & FILTER IMPLEMENTATIONS ==========

    @Override
    public List<Product> searchByName(String name) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByCategory(Integer categoryId) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByInStock(boolean inStock) {
        return productRepository.findAll().stream()
                .filter(p -> p.isInStock() == inStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() != null &&
                        p.getPrice().compareTo(minPrice) >= 0 &&
                        p.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByCategoryAndName(Integer categoryId, String name) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId) &&
                        p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByNameAndPrice(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()) &&
                        p.getPrice() != null &&
                        p.getPrice().compareTo(minPrice) >= 0 &&
                        p.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByCategoryAndPrice(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId) &&
                        p.getPrice() != null &&
                        p.getPrice().compareTo(minPrice) >= 0 &&
                        p.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchAdvanced(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> {
                    // Filter by name if provided
                    if (name != null && !name.isEmpty()) {
                        if (p.getName() == null || !p.getName().toLowerCase().contains(name.toLowerCase())) {
                            return false;
                        }
                    }
                    // Filter by category if provided
                    if (categoryId != null) {
                        if (p.getCategory() == null || !p.getCategory().getId().equals(categoryId)) {
                            return false;
                        }
                    }
                    // Filter by minimum price if provided
                    if (minPrice != null) {
                        if (p.getPrice() == null || p.getPrice().compareTo(minPrice) < 0) {
                            return false;
                        }
                    }
                    // Filter by maximum price if provided
                    if (maxPrice != null) {
                        if (p.getPrice() == null || p.getPrice().compareTo(maxPrice) > 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
