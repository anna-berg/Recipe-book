package com.berg.recipe.services;

import com.berg.recipe.dao.CategoryRecipeDao;
import com.berg.recipe.entity.CategoryRecipe;

import java.util.List;

public class CategoryService {

    private static final CategoryService INSTANCE = new CategoryService();
    private final CategoryRecipeDao categoryRecipeDao = CategoryRecipeDao.getInstance();

    private CategoryService() {
    }

    public List<CategoryRecipe> findAllCategories(){
        return categoryRecipeDao.findAll();
    }

    public static CategoryService getInstance() {
        return INSTANCE;
    }
}
