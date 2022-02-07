package com.berg.recipe.servlet;

import com.berg.recipe.services.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/category-list")
public class CategoryRecipeServlet extends HttpServlet {

    CategoryService categoryService = CategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            writer.write("<h1>Список категорий:</h1>");
            writer.write("<ul>");
            categoryService.findAllCategories().forEach(categoryRecipe -> {
                writer.write("""
                        <li>
                            <a href = "/category?categoryId=%d">%s</a>
                        </li>
                        """.formatted(categoryRecipe.getId(), categoryRecipe.getCategory()));
            });
            writer.write("</ul>");
        }
    }
}
