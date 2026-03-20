package com.uade.tpo.marketplace.service;

import java.util.ArrayList;

import com.uade.tpo.marketplace.entity.Category;

public class CategoryService {
    public ArrayList<Category> getCategories() {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }

    public Category getCategoryById(int categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryById(categoryId);
    } 

    public String createCategory(int CategoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(CategoryId);
    }
}
