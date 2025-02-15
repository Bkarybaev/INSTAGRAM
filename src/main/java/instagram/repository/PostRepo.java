package instagram.repository;

import instagram.models.Image;
import instagram.models.Post;

import java.util.List;

public interface PostRepo {
    List<Post> getPostsByUserId(Long id);

    Post getPostById(Long id);

    void savePost(Post post,Long userId, Image imageUrl);
}
