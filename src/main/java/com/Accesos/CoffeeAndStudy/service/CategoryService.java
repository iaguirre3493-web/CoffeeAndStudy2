package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Category;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> filterCategories(String name, Boolean active, Integer priority) {
        logger.info("Filtering categories with name={}, active={}, priority={}", name, active, priority);

        if (name != null && active != null && priority != null) {
            return categoryRepository.findByNameContainingIgnoreCaseAndActiveAndPriority(name, active, priority);
        }
        if (name != null) {
            return categoryRepository.findByNameContainingIgnoreCase(name);
        }
        if (active != null) {
            return categoryRepository.findByActive(active);
        }
        if (priority != null) {
            return categoryRepository.findByPriority(priority);
        }
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        logger.info("Getting category by id={}", id);

        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found", id);
                    return new ResourceNotFoundException("Category with id " + id + " not found");
                });
    }

    public Category saveCategory(Category category) {
        logger.info("Saving new category with name={}", category.getName());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        logger.info("Updating category with id={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found for update", id);
                    return new ResourceNotFoundException("Category with id " + id + " not found");
                });

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        category.setColorHex(updatedCategory.getColorHex());
        category.setPriority(updatedCategory.getPriority());
        category.setCreatedAt(updatedCategory.getCreatedAt());
        category.setActive(updatedCategory.getActive());

        logger.info("Category with id {} updated successfully", id);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        logger.info("Deleting category with id={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found for delete", id);
                    return new ResourceNotFoundException("Category with id " + id + " not found");
                });

        categoryRepository.delete(category);
        logger.info("Category with id {} deleted successfully", id);
    }
}