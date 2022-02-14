package com.berg.recipe.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryRecipeDto {

    Long id;
    String category;
}

