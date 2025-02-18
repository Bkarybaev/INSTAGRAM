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

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommentRepoImpl implements CommentRepo {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Long userId, Post post, Comment comment) {
        User user = entityManager.find(User.class, userId);
        if (user.getComment() == null) {
            user.setComment(new ArrayList<>());
        }
        user.getComment().add(comment);
        comment.setUser(user);
        comment.setPost(post);
        entityManager.merge(comment);

    }

    @Override
    public List<Comment> getCommentsByPostId(Long id) {
        return entityManager.createQuery("select c from Comment c where c.post.id = :id",Comment.class)
                .setParameter("id",id)
                .getResultList();
    }
}
