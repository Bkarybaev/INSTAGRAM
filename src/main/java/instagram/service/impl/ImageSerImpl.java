package instagram.service.impl;

import instagram.models.Image;
import instagram.models.Post;
import instagram.repository.ImageRepo;
import instagram.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSerImpl implements ImageService {
    private final ImageRepo imageRepo;
    @Override
    public List<Image> getImagesByPosts(List<Post> posts) {
        if (posts == null) {
            return null;
        }
        return imageRepo.getImagesByPosts(posts);
    }
}
