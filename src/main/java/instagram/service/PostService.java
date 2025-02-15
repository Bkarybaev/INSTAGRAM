package instagram.service;

import instagram.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByUserId(Long id);
}
