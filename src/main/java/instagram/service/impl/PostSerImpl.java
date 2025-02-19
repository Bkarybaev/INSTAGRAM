package instagram.service.impl;

import instagram.exeptions.NullComent;
import instagram.models.Image;
import instagram.models.Post;
import instagram.repository.PostRepo;
import instagram.service.PostService;
import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public void likePost(Long postId) {
        postRepo.likePost(postId);
    }

    @Override
    public Post getPostByCommentId(Long commentId) {
        if (commentId == null) {
           throw new NullComent("commentId is null");
        }
        return postRepo.getPostByCommentId(commentId);
    }

    @Override
    public List<Post> getAll() {
        return postRepo.getAll();
    }
}
