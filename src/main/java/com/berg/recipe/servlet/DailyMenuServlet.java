package com.berg.recipe.servlet;

import com.berg.recipe.dto.DailyMenuDto;
import com.berg.recipe.service.DailyMenuService;
import com.berg.recipe.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/daily-menu")
public class DailyMenuServlet extends HttpServlet {

    private final DailyMenuService dailyMenuService = DailyMenuService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var dailyMenuId = Optional.ofNullable(req.getParameter("dailyMenuId"))
                .map(Long::valueOf)
                .orElse(null);

        if (dailyMenuId == null) {
            req.setAttribute("dailyMenuList", dailyMenuService.findAll());
            req.getRequestDispatcher(JspHelper.getPath("daily-menu"))
                    .forward(req, resp);
        } else {
            dailyMenuService.findById(dailyMenuId)
                    .ifPresentOrElse(dailyMenuDto -> successResponse(req, resp, dailyMenuDto), () -> errorResponse(req, resp));

        }
    }

    @SneakyThrows
    private void errorResponse(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("message", "No such daily menu");
        req.getRequestDispatcher(JspHelper.getPath("recipe"))
                .forward(req, resp);
    }

    @SneakyThrows
    private void successResponse(HttpServletRequest req, HttpServletResponse resp, DailyMenuDto dailyMenuDto) {
        req.setAttribute("dailyMenuDto", dailyMenuDto);
        req.getRequestDispatcher(JspHelper.getPath("daily-menu"))
                .forward(req, resp);
    }
}