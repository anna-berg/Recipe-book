package com.berg.recipe.dto;

import com.berg.recipe.entity.CategoryRecipe;
import lombok.Value;

@Value
public class RecipeDto {

    private Long id;
    private CategoryRecipe categoryRecipe;
    private String title;
}
