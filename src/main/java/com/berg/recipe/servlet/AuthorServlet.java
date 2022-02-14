package com.berg.recipe.servlet;

import com.berg.recipe.dto.AuthorDto;
import com.berg.recipe.dto.RecipeFilter;
import com.berg.recipe.services.AuthorService;
import com.berg.recipe.services.RecipeService;
import com.berg.recipe.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/author")
public class AuthorServlet extends HttpServlet {

    private final RecipeService recipeService = RecipeService.getInstance();
    private final AuthorService authorService = AuthorService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var authorId = Optional.ofNullable(req.getParameter("authorId"))
                .map(Long::valueOf)
                .orElse(null);

        int limit = 100;
        int offset = 0;

        if (authorId == null) {
            var authorList = authorService.findAll();
            req.setAttribute("author", authorList);
            req.getRequestDispatcher(JspHelper.getPath("author-list"))
                    .forward(req, resp);
        }

        req.setAttribute("authorName", authorService.findAuthorById(authorId).map(AuthorDto::name).orElse("No such author"));
        var filter = RecipeFilter.builder()
                .limit(limit)
                .offset(offset)
                .authorId(authorId)
                .build();
        req.setAttribute("recipes", recipeService.findAll(filter));
        req.getRequestDispatcher(JspHelper.getPath("author"))
                .forward(req, resp);
    }
}
