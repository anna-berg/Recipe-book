package com.berg.recipe.servlet;

import com.berg.recipe.dto.ProductDto;
import com.berg.recipe.service.ProductService;
import com.berg.recipe.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

     private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var productId = Optional.ofNullable(req.getParameter("productId"))
                .map(Long::valueOf)
                .orElse(null);

        if (productId == null) {
            req.setAttribute("productList", productService.findAll());
            req.getRequestDispatcher(JspHelper.getPath("product"))
                    .forward(req, resp);
        } else {
            productService.findById(productId)
                    .ifPresentOrElse(productDto -> successResponse(req, resp, productDto), () -> errorResponse(req, resp));

        }
    }

    @SneakyThrows
    private void errorResponse(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("message", "No such product");
        req.getRequestDispatcher(JspHelper.getPath("product"))
                .forward(req, resp);
    }

    @SneakyThrows
    private void successResponse(HttpServletRequest req, HttpServletResponse resp, ProductDto productDto) {
        req.setAttribute("product", productDto);
        req.getRequestDispatcher(JspHelper.getPath("product"))
                .forward(req, resp);
    }
}
