package com.berg.recipe.servlet;

import com.berg.recipe.dao.RecipeDao;
import com.berg.recipe.entity.Recipe;
import com.berg.recipe.services.RecipeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/recipe")
public class RecipeServlet extends HttpServlet {

    RecipeService recipeService = RecipeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var recipeId = Long.valueOf(req.getParameter("recipeId"));
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            var recipe = recipeService.findById(recipeId);

            writer.write("<h1>" + recipe.getTitle() + "</h1>");
            writer.write("<h3>" + recipe.getCategoryRecipe().getCategory() + " , " + recipe.getAuthor().getName() + "</h3>");
            writer.write(recipe.getDescription());
            writer.write("<h3> Продукты: </h3>");
            writer.write("<ul>");
            recipeService.findProductsByRecipe(recipeId).forEach(product -> writer.write("""
                    <li>
                        %s
                    </li>
                    """.formatted(product.getName())));
            writer.write("</ul>");
        }
    }
}
