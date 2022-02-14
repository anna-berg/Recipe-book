package com.berg.recipe.servlet;

import com.berg.recipe.dto.RecipeDto;
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

@WebServlet("/recipe")
public class RecipeServlet extends HttpServlet {

    private final RecipeService recipeService = RecipeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var recipeId = Optional.ofNullable(req.getParameter("recipeId"))
                .map(Long::valueOf)
                .orElse(null);

        if (recipeId == null) {
            req.setAttribute("recipeList", recipeService.findAll());
            req.getRequestDispatcher(JspHelper.getPath("recipe"))
                    .forward(req, resp);
        }
        recipeService.findById(recipeId)
                .ifPresentOrElse(recipe -> successResponse(req, resp, recipe), () -> errorResponse(req, resp));

    }

    @SneakyThrows
    private void errorResponse(HttpServletRequest req, HttpServletResponse resp) {
        String message = "No such recipe";
        req.setAttribute("message", message);
        req.getRequestDispatcher(JspHelper.getPath("recipe"))
                .forward(req, resp);
    }

    @SneakyThrows
    private void successResponse(HttpServletRequest req, HttpServletResponse resp, RecipeDto recipe) {
        req.setAttribute("recipe", recipe);
        req.setAttribute("productList", recipeService.findProductsByRecipe(recipe.getId()));
        req.getRequestDispatcher(JspHelper.getPath("recipe"))
                .forward(req, resp);
    }
}
