package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl() {
        categoryRepository = new CategoryRepository();
    }

    public ArrayList<Category> getCategories() {
        return categoryRepository.getCategories();
    }

    public Optional<Category> getCategoryById(int categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    } 

    public Category createCategory(int newCategoryId, String description) throws CategoryDuplicateException{
        ArrayList<Category> categories = categoryRepository.getCategories();
        if (categories.stream().anyMatch(c -> c.getId() == newCategoryId && c.getDescription().equals(description))) {
            throw new CategoryDuplicateException();
        }
        return categoryRepository.createCategory(newCategoryId, description);
    }

    public Optional<Category> updateCategory(int categoryId, String description) {
        return categoryRepository.updateCategory(categoryId, description);
    }

    public boolean deleteCategory(int categoryId) {
        return categoryRepository.deleteCategory(categoryId);
    }
}
