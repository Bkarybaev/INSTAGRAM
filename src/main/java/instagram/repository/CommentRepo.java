package instagram.repository;

import instagram.models.Comment;
import instagram.models.Post;

public interface CommentRepo {
    void save(Long userId, Post post, Comment comment);
}
