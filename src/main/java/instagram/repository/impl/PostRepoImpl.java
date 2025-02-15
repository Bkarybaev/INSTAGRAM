package instagram.repository.impl;

import instagram.models.Post;
import instagram.models.User;
import instagram.repository.PostRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepoImpl implements PostRepo {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Post> getPostsByUserId(Long id) {
       return em.createQuery("SELECT p FROM Post p WHERE p.user.id = :userId", Post.class)
                .setParameter("userId", id)
                .getResultList();
    }
}
