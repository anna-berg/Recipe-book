package com.berg.recipe.servlet;

import com.berg.recipe.services.RecipeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/recipe-list")
public class RecipeListServlet extends HttpServlet {

    RecipeService recipeService = RecipeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            writer.write("<h1>Список рецептов:</h1>");
            recipeService.findAll().forEach(recipeDto -> {
                writer.write("""
                        <li>
                            <a href = "/recipe?recipeId=%d">%s</a>
                        </li>
                        """.formatted(recipeDto.getId(), recipeDto.getTitle()));
            });
            writer.write("</ul>");
        }
    }
}
