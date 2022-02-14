package com.berg.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class RecipeDto {

    Long id;
    CategoryRecipeDto categoryRecipeDto;
    AuthorDto authorDto;
    String description;
    String title;
    List<ProductDto> products;
}
