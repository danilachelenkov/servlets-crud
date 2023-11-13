package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private static final String REQUEST_PROCESSING_STATUS = "ReqProcessing-Status";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);

        final var data = service.all();
        final var gson = new Gson();

        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        try {
            final var gson = new Gson();
            response.getWriter().println(gson.toJson(service.getById(id)));

        } catch (NotFoundException ex) {
            response.setStatus(204);
            response.addHeader(REQUEST_PROCESSING_STATUS, "Not found item");
            response.getWriter().println();
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {

        response.setContentType(APPLICATION_JSON);

        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);

        try {
            Post item = service.getById(post.getId());
        } catch (NotFoundException ex) {
            response.setStatus(204);
            response.addHeader(REQUEST_PROCESSING_STATUS, "Added item as new");
        }

        final var data = service.save(post);

        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {

        if (service.removeById(id)) {
            response.setStatus(200);
        } else {
            response.setStatus(204);
            response.addHeader(REQUEST_PROCESSING_STATUS, "Not found item for delete");
        }
        response.getWriter().println();

    }
}
