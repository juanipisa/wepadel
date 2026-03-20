package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;

public interface CategoryService {
    public ArrayList<Category> getCategories();
    public Optional<Category> getCategoryById(int categoryId);
    public Category createCategory(int newCategoryId, String description) throws CategoryDuplicateException;
}
