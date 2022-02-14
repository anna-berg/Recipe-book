package com.berg.recipe.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RecipeFilter {
    int limit;
    int offset;
    Long recipeId;
    Long authorId;
    Long categoryId;
}
