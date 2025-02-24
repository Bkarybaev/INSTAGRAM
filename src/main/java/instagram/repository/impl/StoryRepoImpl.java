package instagram.repository.impl;

import instagram.models.Post;
import instagram.models.Story;
import instagram.models.User;
import instagram.repository.StoryRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class StoryRepoImpl implements StoryRepo {
    private final EntityManager entityManager;

    @Override
    public void save(Post post, User user) {
//        entityManager.getTransaction().begin();
//       Post post1 = entityManager.find(Post.class, post.getId());
//        User user1 = entityManager.find(User.class, user.getId());
//        entityManager.createNativeQuery("insert into story (createdat, post_id, user_id)values (?,?,?)")
//                .setParameter(1, Timestamp.valueOf(LocalDateTime.now()))
//                .setParameter(2, post1.getId())
//                .setParameter(3, user1.getId())
//                .executeUpdate();
//        Story story = entityManager.createQuery("select s from Story s where s.post.id = :post_id and s.user.id = :user_id", Story.class)
//                .setParameter("post_id", post.getId())
//                .setParameter("user_id", user.getId())
//                .getSingleResult();
//        if (user1.getStories() == null) {
//            user1.setStories(new ArrayList<>());
//        }
//        user1.getStories().add(story);
//        if (post1.getStories() == null) {
//            post1.setStories(new ArrayList<>());
//        }
//        post1.getStories().add(story);
//        story.setPost(post);
//        story.setUser(user);
//        entityManager.merge(story);
//        entityManager.merge(post);
//        entityManager.merge(user);
//        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();

        // Пост жана колдонуучуну табуу
        Post post1 = entityManager.find(Post.class, post.getId());
        User user1 = entityManager.find(User.class, user.getId());

        try {
            // Сториде буга чейин барбы?
            Story story = entityManager.createQuery(
                            "SELECT s FROM Story s WHERE s.post.id = :post_id AND s.user.id = :user_id",
                            Story.class)
                    .setParameter("post_id", post.getId())
                    .setParameter("user_id", user.getId())
                    .getSingleResult();

            // Эгер бар болсо, өчүрүү
            entityManager.remove(story);

            // User жана Post тизмелеринен өчүрүү
            user1.getStories().remove(story);
            post1.getStories().remove(story);
        } catch (NoResultException e) {
            // Эгер жок болсо, жаңы сторини кошуу
            Story newStory = new Story();
            newStory.setPost(post1);
            newStory.setUser(user1);
            newStory.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());

            entityManager.persist(newStory);

            // Колдонуучу жана постко кошуу
            if (user1.getStories() == null) {
                user1.setStories(new ArrayList<>());
            }
            user1.getStories().add(newStory);

            if (post1.getStories() == null) {
                post1.setStories(new ArrayList<>());
            }
            post1.getStories().add(newStory);
        }

        entityManager.merge(post1);
        entityManager.merge(user1);
        entityManager.getTransaction().commit();

    }

    @Override
    public List<Story> findAll() {
        return entityManager.createQuery("from Story", Story.class).getResultList();
    }

    @Override
        public boolean isStoryExists(Long postId, Long userId) {
            Long count = entityManager.createQuery(
                            "SELECT COUNT(s) FROM Story s WHERE s.post.id = :postId AND s.user.id = :userId", Long.class)
                    .setParameter("postId", postId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        System.err.println((count != null && count > 0 ) +"\n\n\n\n\n");
            return count != null && count > 0;
        }
}
