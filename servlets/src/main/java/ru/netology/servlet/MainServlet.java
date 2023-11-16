package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfiguration;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    private final static String METHOD_GET = "GET";
    private final static String METHOD_POST = "POST";
    private final static String METHOD_DELETE = "DELETE";
    private final static String API_POSTS = "/api/posts";
    private final static String API_POSTS_ID = "/api/posts/\\d+";


    private PostController controller;

    @Override
    public void init() {
       /* final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);*/

        final var context = new AnnotationConfigApplicationContext(JavaConfiguration.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            // primitive routing
            if (method.equals(METHOD_GET) && path.equals(API_POSTS)) {
                controller.all(resp);
                return;
            }

            if (method.equals(METHOD_GET) && path.matches(API_POSTS_ID)) {
                // easy way
                controller.getById(getId(path), resp);
                return;
            }

            if (method.equals(METHOD_POST) && path.equals(API_POSTS)) {
                controller.save(req.getReader(), resp);
                return;
            }

            if (method.equals(METHOD_DELETE) && path.matches(API_POSTS_ID)) {
                // easy way
                controller.removeById(getId(path), resp);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long getId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }

}

