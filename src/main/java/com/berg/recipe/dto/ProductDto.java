package com.berg.recipe.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDto {

    Long id;
    String name;
    Integer proteins;
    Integer fats;
    Integer carbohydrates;
    String type;
}
