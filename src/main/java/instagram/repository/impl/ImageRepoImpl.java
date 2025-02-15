package instagram.repository.impl;

import instagram.models.Image;
import instagram.models.Post;
import instagram.models.User;
import instagram.repository.ImageRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class ImageRepoImpl implements ImageRepo {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Image> getImagesByPosts(List<Post> posts){
        List<Image> images = new ArrayList<>();
        for (Post post : posts) {
            images.add(em.createQuery("select i from Image i where i.post.id = :postId", Image.class)
                .setParameter("postId", post.getId())
                .getSingleResult());
        }
        return images;
    }
}
