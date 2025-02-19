package instagram.repository.impl;

import instagram.models.*;
import instagram.repository.PostRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PostRepoImpl implements PostRepo {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Post> getPostsByUserId(Long id) {
        return em.createQuery("SELECT p FROM Post p WHERE p.user.id = :userId", Post.class)
                .setParameter("userId", id)
                .getResultList();
    }

    @Override
    public Post getPostById(Long id) {
        return em.find(Post.class, id);
    }

    @Override
    public void savePost(Post post, Long userId, Image imageUrl) {

        em.createNativeQuery("""
                            INSERT INTO posts (title, description, createdat, user_id)
                            VALUES (?,?,?, ?)
                        """)
                .setParameter(1, post.getTitle())
                .setParameter(2, post.getDescription())
                .setParameter(3, LocalDate.now())
                .setParameter(4, userId)
                .executeUpdate();
        Post singleResult = em.createQuery("select p from Post p where p.user.id = :userId and p.title = :title and p.description = :description", Post.class)
                .setParameter("userId", userId)
                .setParameter("title", post.getTitle())
                .setParameter("description", post.getDescription())
                .getSingleResult();

        em.createNativeQuery("""
                            INSERT INTO images (imageurl, post_id, user_id)
                            VALUES (?, ?, ?)
                        """)
                .setParameter(1, imageUrl.getImageUrl())
                .setParameter(2, singleResult.getId())
                .setParameter(3, userId)
                .executeUpdate();


        Like like = new Like();
        Comment comment = new Comment();
        like.setComment(comment);
        comment.setPost(singleResult);
        if (comment.getLikes() == null) {
            comment.setLikes(new ArrayList<>());
        }
        like.setPost(singleResult);
        if (singleResult.getLikes() == null) {
            singleResult.setLikes(new ArrayList<>());
        }
        em.persist(comment);
        em.persist(like);

    }

    @Override
    public void likePost(Long postId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, UserRepoImpl.user.getId());
        if (user == null) {
            user = em.find(User.class, UserRepoImpl.user1.getId());
        }
        List<Like> likes = em.createQuery("select l from Like l where l.post.id = :postId and l.user.id = :userId", Like.class)
                .setParameter("postId", postId)
                .setParameter("userId", user.getId())
                .getResultList();
        Like like = likes.isEmpty() ? null : likes.get(0);
        if (like != null) {
            if (em.contains(like)) {
                user.getLikes().remove(like);
                post.getLikes().remove(like);
                em.remove(like);
            }
        } else {
            like = new Like();
            if (user.getLikes() == null) {
                user.setLikes(new ArrayList<>());
            }
            user.getLikes().add(like);
            like.setUser(user);
            like.setPost(post);
            like.setLike(true);

            if (post.getLikes() == null) {
                post.setLikes(new ArrayList<>());
            }
            post.getLikes().add(like);
            em.persist(like);
        }

        em.merge(post);
        em.merge(user);
    }


    @Override
    public Post getPostByCommentId(Long commentId) {
        return em.createQuery("select c.post from Comment c where c.id = :commentId", Post.class)
                .setParameter("commentId",commentId)
                .getSingleResult();

    }

    @Override
    public List<Post> getAll() {
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }
}
