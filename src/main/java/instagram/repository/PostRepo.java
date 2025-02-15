package instagram.repository;

import instagram.models.Post;

import java.util.List;

public interface PostRepo {
    List<Post> getPostsByUserId(Long id);
}
