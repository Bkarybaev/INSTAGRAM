package instagram.repository;

import instagram.models.Post;
import instagram.models.Story;
import instagram.models.User;

import java.util.List;

public interface StoryRepo {
    void save(Post post, User user);

    List<Story> findAll();

    boolean isStoryExists(Long id, Long id1);
}
