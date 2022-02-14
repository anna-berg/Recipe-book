package com.berg.recipe.services;

import com.berg.recipe.dao.DailyMenuDao;
import com.berg.recipe.dto.AuthorDto;
import com.berg.recipe.dto.CategoryRecipeDto;
import com.berg.recipe.dto.DailyMenuDto;
import com.berg.recipe.dto.RecipeDto;
import com.berg.recipe.entity.Recipe;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class DailyMenuServices {

    private static final DailyMenuServices INSTANCE = new DailyMenuServices();
    private final DailyMenuDao dailyMenuDao = DailyMenuDao.getInstance();

    private DailyMenuServices() {
    }

    public List<DailyMenuDto> findAll() {
        return dailyMenuDao.findAll().stream()
                .map(dailyMenu -> DailyMenuDto.builder()
                        .id(dailyMenu.getId())
                        .breakfast(buildRecipeDto(dailyMenu.getBreakfast()))
                        .firstSnack(buildRecipeDto(dailyMenu.getFirstSnack()))
                        .lunch(buildRecipeDto(dailyMenu.getLunch()))
                        .secondSnack(buildRecipeDto(dailyMenu.getSecondSnack()))
                        .dinner(buildRecipeDto(dailyMenu.getDinner()))
                        .title(dailyMenu.getTitle())
                        .build())
                .collect(toList());
    }

    public static DailyMenuServices getInstance() {
        return INSTANCE;
    }

    public Optional<DailyMenuDto> findById(Long dailyMenuId) {
        return dailyMenuDao.findById(dailyMenuId)
                .map(dailyMenu -> DailyMenuDto.builder()
                        .id(dailyMenu.getId())
                        .breakfast(buildRecipeDto(dailyMenu.getBreakfast()))
                        .firstSnack(buildRecipeDto(dailyMenu.getFirstSnack()))
                        .lunch(buildRecipeDto(dailyMenu.getLunch()))
                        .secondSnack(buildRecipeDto(dailyMenu.getSecondSnack()))
                        .dinner(buildRecipeDto(dailyMenu.getDinner()))
                        .build());
    }

    private static RecipeDto buildRecipeDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .categoryRecipeDto(CategoryRecipeDto.builder()
                        .id(recipe.getCategoryRecipe().getId())
                        .category(recipe.getCategoryRecipe().getCategory())
                        .build())
                .authorDto(new AuthorDto(recipe.getAuthor().getId(),
                        recipe.getAuthor().getName()))
                .description(recipe.getDescription())
                .title(recipe.getTitle())
                .products(ProductService.getInstance().findProductsByRecipe(recipe.getId()))
                .build();
    }
}
