package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Category;

public class CategoryRepository{
    public ArrayList<Category> categories = new ArrayList<>(
        Arrays.asList(Category.builder().description("Paletas").id(1).build(),
        Category.builder().description("Accesorios").id(2).build(),
        Category.builder().description("Pelotas").id(3).build()));

        public ArrayList<Category> getCategories() {
            return this.categories;
        }
    
        public Optional<Category> getCategoryById(int categoryId) {
            return this.categories.stream().filter(c -> c.getId() == categoryId).findAny();
        } 
    
        public Category createCategory(int categoryId, String description) {
            Category newCategory = Category.builder().id(categoryId).description(description).build();
            this.categories.add(newCategory);
            return newCategory;
        }
}
