package instagram.service;

import instagram.models.Comment;
import instagram.models.Post;

public interface CommentService {
    void save(Long id, Post postById, Comment comment);
}
