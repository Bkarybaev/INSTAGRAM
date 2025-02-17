package instagram.repository.impl;

import instagram.models.Comment;
import instagram.models.Post;
import instagram.models.User;
import instagram.repository.CommentRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommentRepoImpl implements CommentRepo {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Long userId, Post post, Comment comment) {
        User user = entityManager.find(User.class, userId);
        user.setComment(comment);
        comment.setUser(user);
        comment.setPost(post);
        entityManager.persist(comment);
    }
}
