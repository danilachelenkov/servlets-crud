package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.stream.Collectors;

// Stub

public class PostRepository {
    private final Map<Long, String> memory = new HashMap<>();

    public List<Post> all() {

        synchronized (memory) {
            return memory.entrySet().stream()
                    .map(a -> new Post(a.getKey(), a.getValue()))
                    .collect(Collectors.toList());
        }

    }

    public Optional<Post> getById(long id) {

        synchronized (memory) {
            if (memory.containsKey(id)) {
                return Optional.of(new Post(id, memory.get(id)));
            } else {
                return Optional.empty();
            }
        }

    }

    public Post save(Post post) throws NumberFormatException {

        synchronized (memory) {
            memory.put(post.getId(), post.getContent());
        }
        return post;
    }

    public boolean removeById(long id) {

        synchronized (memory) {
            if (memory.containsKey(id)) {
                memory.remove(id);
                return true;
            } else {
                return false;
            }

        }
    }
}
