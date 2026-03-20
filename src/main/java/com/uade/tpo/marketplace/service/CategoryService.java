package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.repository.CategoryRepository;

public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService() {
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
}
