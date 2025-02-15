package instagram.repository.impl;

import instagram.models.Image;
import instagram.models.Post;
import instagram.models.User;
import instagram.repository.PostRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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




    }
}
