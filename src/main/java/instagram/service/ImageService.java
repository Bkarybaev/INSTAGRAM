package instagram.service;

import instagram.models.Image;
import instagram.models.Post;

import java.util.List;

public interface ImageService {
    List<Image> getImagesByPosts(List<Post> posts);
}
