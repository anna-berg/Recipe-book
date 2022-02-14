package com.berg.recipe.services;

import com.berg.recipe.dao.ProductDao;
import com.berg.recipe.dao.RecipeDao;
import com.berg.recipe.dto.AuthorDto;
import com.berg.recipe.dto.CategoryRecipeDto;
import com.berg.recipe.dto.ProductDto;
import com.berg.recipe.dto.RecipeDto;
import com.berg.recipe.dto.RecipeFilter;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class RecipeService {

    private static final RecipeService INSTANCE = new RecipeService();
    private final RecipeDao recipeDao = RecipeDao.getInstance();
    private final ProductDao productDao = ProductDao.getInstance();

    private RecipeService() {
    }

    public List<ProductDto> findProductsByRecipe(Long recipeId) {
        return productDao.findProductByRecipe(recipeId).stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .proteins(product.getProteins())
                        .fats(product.getFats())
                        .carbohydrates(product.getCarbohydrates())
                        .type(product.getType())
                        .build()
                )
                .collect(toList());

    }

    public List<RecipeDto> findAll() {
        return recipeDao.findAll().stream()
                .map(recipe -> RecipeDto.builder()
                        .id(recipe.getId())
                        .categoryRecipeDto(CategoryRecipeDto.builder()
                                .id(recipe.getCategoryRecipe().getId())
                                .category(recipe.getCategoryRecipe().getCategory())
                                .build()
                        )
                        .authorDto(new AuthorDto(recipe.getAuthor().getId(),
                                recipe.getAuthor().getName()))
                        .description(recipe.getDescription())
                        .title(recipe.getTitle())
                        .build()
                )
                .collect(toList());
    }

    public List<RecipeDto> findAll(RecipeFilter filter) {
        return recipeDao.findAll(filter).stream()
                .map(recipe -> RecipeDto.builder()
                        .id(recipe.getId())
                        .categoryRecipeDto(CategoryRecipeDto.builder()
                                .id(recipe.getCategoryRecipe().getId())
                                .category(recipe.getCategoryRecipe().getCategory())
                                .build()
                        )
                        .authorDto(new AuthorDto(recipe.getAuthor().getId(),
                                recipe.getAuthor().getName()))
                        .description(recipe.getDescription())
                        .title(recipe.getTitle())
                        .build()
                )
                .collect(toList());
    }

    public Optional<RecipeDto> findById(Long id) {
        return recipeDao.findById(id)
                .map(recipe -> RecipeDto.builder()
                        .id(recipe.getId())
                        .categoryRecipeDto(CategoryRecipeDto.builder()
                                .id(recipe.getCategoryRecipe().getId())
                                .category(recipe.getCategoryRecipe().getCategory())
                                .build()
                        )
                        .authorDto(new AuthorDto(recipe.getAuthor().getId(),
                                recipe.getAuthor().getName()))
                        .description(recipe.getDescription())
                        .title(recipe.getTitle())
                        .build()
                );
    }

    public static RecipeService getInstance() {
        return INSTANCE;
    }
}
