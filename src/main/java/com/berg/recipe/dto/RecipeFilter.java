package com.berg.recipe.dto;

public record RecipeFilter(int limit,
                           int offset,
                           Long recipeId,
                           Long authorId,
                           Long categoryId) {
}
