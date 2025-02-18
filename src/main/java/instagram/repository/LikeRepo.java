package instagram.repository;

import instagram.models.Post;
import instagram.models.User;

public interface LikeRepo {
    void likeComment(Long commentId, User user);
}
