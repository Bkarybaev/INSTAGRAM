package instagram.repository.impl;

import instagram.models.Comment;
import instagram.models.Like;
import instagram.models.Post;
import instagram.models.User;
import instagram.repository.LikeRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
@RequiredArgsConstructor
public class LikeRepoImpl implements LikeRepo {
@PersistenceContext
private final EntityManager entityManager;
    @Override
    public void likeComment(Long commentId, User user) {

            List<Like> likes = entityManager.createQuery(
                            "select l from Like l where l.comment.id = :comId and l.user.id = :userId", Like.class)
                    .setParameter("comId", commentId)
                    .setParameter("userId", user.getId())
                    .getResultList();

            Comment comment = entityManager.find(Comment.class, commentId);
            Like like = likes.isEmpty() ? null : likes.get(0);

            if (like != null) {
                like = entityManager.merge(like);
                user.getLikes().remove(like);
                comment.getLikes().remove(like);
                entityManager.remove(like);
            } else {
                like = new Like();
                like.setUser(user);
                like.setComment(comment);

                if (user.getLikes() == null) {
                    user.setLikes(new ArrayList<>());
                }
                user.getLikes().add(like);

                if (comment.getLikes() == null) {
                    comment.setLikes(new ArrayList<>());
                }
                comment.getLikes().add(like);

                entityManager.persist(like);
            }

            entityManager.merge(comment);
            entityManager.merge(user);
    }
}
