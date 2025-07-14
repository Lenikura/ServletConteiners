package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/api/posts/*")
public class PostServlet extends HttpServlet {
    private final PostRepository repository = new PostRepository();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            writeJson(resp, repository.all());
        } else {
            long id = parseId(path);
            repository.getById(id)
                    .ifPresentOrElse(
                            post -> {
                                try {
                                    writeJson(resp, post);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            () -> resp.setStatus(HttpServletResponse.SC_NOT_FOUND));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Post post = gson.fromJson(req.getReader(), Post.class);
        Post saved = repository.save(post);
        writeJson(resp, saved);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = parseId(req.getPathInfo());
        repository.removeById(id);
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(data));
    }

    private long parseId(String path) {
        try {
            return Long.parseLong(path.substring(1));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid ID in path");
        }
    }
}