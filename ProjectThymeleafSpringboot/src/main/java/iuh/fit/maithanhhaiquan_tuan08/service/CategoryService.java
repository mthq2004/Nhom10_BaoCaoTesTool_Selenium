package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category findById(Integer id);

    Category save(Category category);

    void deleteById(Integer id);
}
