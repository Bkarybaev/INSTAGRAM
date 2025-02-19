package instagram.service;

import instagram.models.Comment;
import instagram.models.Post;

import java.util.List;

public interface CommentService {
    void save(Long id, Post postById, Comment comment);

    List<Comment> getCommentsByPostId(Long id);

    Comment getCommentById(Long commentId);

    void deletedComment(Long id);
}
