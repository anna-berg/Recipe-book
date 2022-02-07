package com.berg.recipe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Stream;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var headers = req.getHeaderNames();
        while (headers.hasMoreElements()){
            var header = headers.nextElement();
            System.out.println(req.getHeader(header));
        }
        resp.setContentType("text/html");
        resp.setHeader("token", "12345");
        try (var writer = resp.getWriter()) {
            writer.write("Hello from First Servlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var reader = req.getReader();
             var lines = reader.lines()) {
            lines.forEach(System.out::println);
        }
    }
}
