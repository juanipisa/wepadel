package com.uade.tpo.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.service.CategoryService;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("categories")
public class CategoryController {
    
    @GetMapping
    public ArrayList<Category> getCategories() {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }

    @GetMapping("{categoryId}")
    public Category getCategoryById(@PathVariable int categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public String createCategory(@RequestBody int categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(categoryId);
    }
}
