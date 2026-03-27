package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    } 

     public Category createCategory(String description) throws CategoryDuplicateException{
        List<Category> categories = categoryRepository.findAll();
        if (categories.stream().anyMatch(c -> c.getDescription().equals(description))) {
            throw new CategoryDuplicateException();
        }
        return categoryRepository.save(new Category(description));
    }

    // public Optional<Category> updateCategory(Long categoryId, String description) {
    //    return categoryRepository.updateCategory(categoryId, description);
    //}

    //public boolean deleteCategory(Long categoryId) {
    //    return categoryRepository.deleteCategory(categoryId);
    //}
}
