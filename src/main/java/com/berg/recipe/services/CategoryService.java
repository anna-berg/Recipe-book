package com.berg.recipe.services;

import com.berg.recipe.dao.CategoryRecipeDao;
import com.berg.recipe.dto.CategoryRecipeDto;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CategoryService {

    private static final CategoryService INSTANCE = new CategoryService();
    private final CategoryRecipeDao categoryRecipeDao = CategoryRecipeDao.getInstance();

    private CategoryService() {
    }

    public Optional<CategoryRecipeDto> findCategoryById(Long id) {
        return categoryRecipeDao.findById(id)
                .map(categoryRecipeDto -> CategoryRecipeDto.builder()
                        .id(categoryRecipeDto.getId())
                        .category(categoryRecipeDto.getCategory())
                        .build());
    }

    public List<CategoryRecipeDto> findAllCategories() {
        return categoryRecipeDao.findAll().stream()
                .map(categoryRecipe -> CategoryRecipeDto.builder()
                        .id(categoryRecipe.getId())
                        .category(categoryRecipe.getCategory())
                        .build())
                .collect(toList());
    }

    public static CategoryService getInstance() {
        return INSTANCE;
    }
}
