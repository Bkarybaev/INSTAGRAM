package instagram.repository;

import instagram.models.Image;
import instagram.models.Post;

import java.util.List;

public interface ImageRepo {
    List<Image> getImagesByPosts(List<Post> posts);
}
