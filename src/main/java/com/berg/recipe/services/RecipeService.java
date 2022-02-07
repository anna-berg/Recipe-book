package com.berg.recipe.services;

import com.berg.recipe.dao.RecipeDao;
import com.berg.recipe.dto.RecipeDto;
import com.berg.recipe.dto.RecipeFilter;
import com.berg.recipe.entity.Product;
import com.berg.recipe.entity.Recipe;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeService {

    private static final RecipeService INSTANCE = new RecipeService();
    private final RecipeDao recipeDao = RecipeDao.getInstance();

    private RecipeService() {
    }

    public List<RecipeDto> findAll (){
        return recipeDao.findAll().stream()
                .map(recipe -> new RecipeDto(
                        recipe.getId(),
                        recipe.getCategoryRecipe(),
                        recipe.getTitle()
                ))
                .collect(Collectors.toList());
    }

    public List<Recipe> findAll(RecipeFilter filter){
        return recipeDao.findAll(filter);
    }

    public Recipe findById (Long id){
        return recipeDao.findById(id).orElse(null);
    }

    public List<Product> findProductsByRecipe(Long id){
        return recipeDao.findProductByRecipe(id);
    }

    public static RecipeService getInstance() {
        return INSTANCE;
    }
}
