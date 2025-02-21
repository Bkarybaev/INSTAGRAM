package instagram.service;

import instagram.models.Image;
import instagram.models.Post;
import instagram.models.User;

import java.util.List;

public interface PostService {
    List<Post> getPostsByUserId(Long id);

    Post getPostById(Long id);

    void savePost(Post post, Long userId, Image imageUrl,List<User> taggedUsers);

    void likePost(Long postId);

    Post getPostByCommentId(Long commentId);

    List<Post> getAll();

    void save(Post post);

    Long delete(Long postId);
}
