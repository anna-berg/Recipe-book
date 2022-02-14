package com.berg.recipe.servlet;

import com.berg.recipe.dto.CategoryRecipeDto;
import com.berg.recipe.dto.RecipeFilter;
import com.berg.recipe.services.CategoryService;
import com.berg.recipe.services.RecipeService;
import com.berg.recipe.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/category")
public class CategoryRecipeServlet extends HttpServlet {

    private final CategoryService categoryService = CategoryService.getInstance();
    private final RecipeService recipeService = RecipeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var categoryId = Optional.ofNullable(req.getParameter("categoryId"))
                .map(Long::valueOf)
                .orElse(null);

        if (categoryId == null) {
            var categoryList = categoryService.findAllCategories();
            req.setAttribute("categoryList", categoryList);
            req.getRequestDispatcher(JspHelper.getPath("category"))
                    .forward(req, resp);
        }

        categoryService.findCategoryById(categoryId)
                .ifPresentOrElse(categoryRecipeDto -> successResponse(categoryRecipeDto, req, resp),
                        () -> errorResponse(req, resp));
    }

    @SneakyThrows
    private void errorResponse(HttpServletRequest req, HttpServletResponse resp) {
        String message = "No such category";
        req.setAttribute("message", message);
        req.getRequestDispatcher(JspHelper.getPath("category"))
                .forward(req, resp);
    }

    @SneakyThrows
    private void successResponse(CategoryRecipeDto categoryDto, HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("categoryDto", categoryDto);
        RecipeFilter filter = RecipeFilter.builder()
                .categoryId(categoryDto.getId())
                .limit(100)
                .build();
        req.setAttribute("recipeListByCategory", recipeService.findAll(filter));
        req.getRequestDispatcher(JspHelper.getPath("category"))
                .forward(req, resp);
    }
}
