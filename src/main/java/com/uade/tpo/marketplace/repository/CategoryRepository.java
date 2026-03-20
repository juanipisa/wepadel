package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;

import com.uade.tpo.marketplace.entity.Category;

public class CategoryRepository{
    public ArrayList<Category> categories = new ArrayList<>(
        Arrays.asList(Category.builder().description("Paletas").id(1).build(),
        Category.builder().description("Accesorios").id(2).build(),
        Category.builder().description("Pelotas").id(3).build()));

        public ArrayList<Category> getCategories() {
            return this.categories;
        }
    
        public Category getCategoryById(int categoryId) {
            return null;
        } 
    
        public String createCategory(String entity) {
            return null;
        }
}
