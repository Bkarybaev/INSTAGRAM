package instagram.service.impl;

import instagram.models.Image;
import instagram.models.Post;
import instagram.repository.PostRepo;
import instagram.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSerImpl implements PostService {
    private final PostRepo postRepo;
    @Override
    public List<Post> getPostsByUserId(Long id) {
        return postRepo.getPostsByUserId(id);
    }

    @Override
    public Post getPostById(Long id) {
        if (id == null) {
            return null;
        }
        return postRepo.getPostById(id);
    }

    @Override
    public void savePost(Post post,Long userId, Image imageUrl) {
        postRepo.savePost(post,userId,imageUrl);
    }
}
