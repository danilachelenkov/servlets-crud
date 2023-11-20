package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {
    private final Map<Long, Post> memory = new HashMap<>();

    public List<Post> all() {

        synchronized (memory) {
            return memory.entrySet().stream()
                    .filter(a -> a.getValue().isDeleted() == false)
                    .map(a->a.getValue())
                    .collect(Collectors.toList());
        }

    }

    public Optional<Post> getById(long id) {

        synchronized (memory) {
            if (memory.containsKey(id)) {
                if (!memory.get(id).isDeleted()) {
                    return Optional.of(memory.get(id));
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<Post> save(Post post) throws NumberFormatException {

        synchronized (memory) {
            if (memory.containsKey(post.getId())) {
                if (!memory.get(post.getId()).isDeleted()) {
                    memory.put(post.getId(), post);
                    return Optional.of(post);
                } else {
                    return Optional.empty();
                }
            } else {
                memory.put(post.getId(), post);
                return Optional.of(post);
            }
        }

    }

    public boolean removeById(long id) {

        synchronized (memory) {
            if (memory.containsKey(id)) {
                memory.get(id).setDeleted(true);
                return true;
            } else {
                return false;
            }

        }
    }
}
