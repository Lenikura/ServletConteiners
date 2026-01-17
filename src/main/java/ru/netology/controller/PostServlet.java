package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.config.AppConfig;
import ru.netology.model.Post;
import ru.netology.service.PostService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/posts/*")
public class PostServlet extends HttpServlet {
    private PostService service;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(PostService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            writeJson(resp, service.all());
        } else {
            long id = parseId(path);
            Post post = service.getById(id);
            if (post != null) {
                writeJson(resp, post);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Post not found\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Post post = gson.fromJson(req.getReader(), Post.class);
        Post saved = service.save(post);
        writeJson(resp, saved);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = parseId(req.getPathInfo());
        service.removeById(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(data));
    }

    private long parseId(String path) {
        try {
            return Long.parseLong(path.substring(1));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid ID in path: " + path);
        }
    }
}