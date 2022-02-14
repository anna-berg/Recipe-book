package com.berg.recipe.template;

import com.berg.recipe.dto.RecipeDto;
import lombok.SneakyThrows;

import java.io.Writer;

public class TemplateEngine {

    @SneakyThrows
    public static void showRecipe(RecipeDto recipeDto, Writer writer) {
        writer.write("<h2>" + recipeDto.getCategoryRecipeDto().getCategory() + ": " + recipeDto.getTitle() + "</h2>");
        writer.write(recipeDto.getDescription());
    }
}
