package com.berg.recipe.servlet;

import com.berg.recipe.dto.RecipeFilter;
import com.berg.recipe.services.AuthorService;
import com.berg.recipe.services.RecipeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/author")
public class AuthorServlet extends HttpServlet {

    RecipeService recipeService = RecipeService.getInstance();
    AuthorService authorService = AuthorService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var authorIdString = req.getParameter("authorId");
        Long authorId = null;
        if (authorIdString != null) {
            authorId = Long.valueOf(authorIdString);
        }

//        var authorId = Optional.ofNullable(req.getParameter("authorId"));
//        Long id = authorId.ifPresent(Long::valueOf);

        int limit = 100;
        int offset = 0;

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        RecipeFilter filter = new RecipeFilter(limit, offset, null, authorId, null);

        try (var writer = resp.getWriter()) {
            if (authorId == null) {
                authorService.findAll().forEach(author -> {
                    writer.write("""
                             <li>
                                <a href = "/author?authorId=%d">%s</a>              
                            </li>
                                """.formatted(author.getId(), author.getName()));
                });
            }

            writer.write("<h1>" + authorService.findAuthorById(authorId).getName() + "</h1>");
            writer.write("<ul>");
            if (authorId != null) {
                recipeService.findAll(filter).forEach(recipe -> writer.write("""
                        <li>
                            %s: <a href = "/recipe?recipeId=%d">%s</a>              
                        </li>
                        """.formatted(recipe.getCategoryRecipe().getCategory(), recipe.getId(), recipe.getTitle())));
                writer.write("</ul>");
            } else {
                recipeService.findAll().forEach(recipeDto -> writer.write("""
                        <li>
                            %s: <a href = "/recipe?recipeId=%d">%s</a>              
                        </li>
                        """.formatted(recipeDto.getCategoryRecipe(), recipeDto.getId(), recipeDto.getTitle())));
            }
        }
    }
}
