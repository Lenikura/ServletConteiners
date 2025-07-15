package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

public class PostServlet extends HttpServlet {
    private PostService service;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");
        service = context.getBean(PostService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}