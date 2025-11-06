package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.Category;
import iuh.fit.maithanhhaiquan_tuan08.repository.CategoryRepository;
import iuh.fit.maithanhhaiquan_tuan08.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Integer id) {
        // Sử dụng query fetch để đảm bảo products được load trước khi rời transaction
        return categoryRepository.findByIdWithProducts(id)
                .orElseGet(() -> categoryRepository.findById(id).orElse(null));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}
