package com.berg.recipe.servlet;

import com.berg.recipe.dto.AuthorDto;
import com.berg.recipe.dto.RecipeFilter;
import com.berg.recipe.service.AuthorService;
import com.berg.recipe.service.RecipeService;
import com.berg.recipe.util.JspHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.util.Optional;

@WebServlet("/author")
public class AuthorServlet extends HttpServlet {

    private final RecipeService recipeService = RecipeService.getInstance();
    private final AuthorService authorService = AuthorService.getInstance();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Optional.ofNullable(req.getParameter("authorId"))
                .map(Long::valueOf)
                .ifPresentOrElse((authorId1) -> ifAuthorIdPresent(req, resp, authorId1), () -> allAuthors(req, resp));
    }

    @SneakyThrows
    private void allAuthors(HttpServletRequest req, HttpServletResponse resp) {
        var authorList = authorService.findAll();
        req.setAttribute("author", authorList);
        req.getRequestDispatcher(JspHelper.getPath("author-list"))
                .forward(req, resp);
    }

    @SneakyThrows
    private void ifAuthorIdPresent(HttpServletRequest req, HttpServletResponse resp, Long authorId) {
        int limit = 100;
        int offset = 0;
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
