package instagram.service.impl;

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
}
