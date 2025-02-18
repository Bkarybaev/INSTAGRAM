package instagram.service;

import instagram.models.Post;
import instagram.models.User;

public interface LikeService {
    void likeComment(Long commentId, User user);
}
