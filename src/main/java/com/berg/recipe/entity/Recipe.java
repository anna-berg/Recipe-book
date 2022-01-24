package com.berg.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Recipe {

    private Long id;
    private String title;
    private Author author;
    private String description;
    private String measure;
    private CategoryRecipe categoryRecipe;
}
