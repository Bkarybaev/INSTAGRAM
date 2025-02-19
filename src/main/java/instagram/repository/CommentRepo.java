package instagram.repository;

import instagram.models.Comment;
import instagram.models.Post;

import java.util.List;

public interface CommentRepo {
    void save(Long userId, Post post, Comment comment);

    List<Comment> getCommentsByPostId(Long id);

    Comment getCommentById(Long commentId);

    void deletedComment(Long id);
}
