package com.berg.recipe.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DailyMenuDto {

    Long id;
    RecipeDto breakfast;
    RecipeDto firstSnack;
    RecipeDto lunch;
    RecipeDto secondSnack;
    RecipeDto dinner;
    String title;
}
