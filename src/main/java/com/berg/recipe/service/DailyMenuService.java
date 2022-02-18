package com.berg.recipe.service;

import com.berg.recipe.dao.DailyMenuDao;
import com.berg.recipe.dto.AuthorDto;
import com.berg.recipe.dto.CategoryRecipeDto;
import com.berg.recipe.dto.DailyMenuDto;
import com.berg.recipe.dto.RecipeDto;
import com.berg.recipe.entity.Recipe;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class DailyMenuService {

    private static final DailyMenuService INSTANCE = new DailyMenuService();
    private final DailyMenuDao dailyMenuDao = DailyMenuDao.getInstance();
    private final ProductService productService = ProductService.getInstance();
    
    private DailyMenuService() {
    }

    public static DailyMenuService getInstance() {
        return INSTANCE;
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

    private RecipeDto buildRecipeDto(Recipe recipe) {
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
                .products(productService.findProductsByRecipe(recipe.getId()))
                .build();
    }
}
