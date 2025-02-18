package instagram.service.impl;

import instagram.exeptions.NullComent;
import instagram.models.Comment;
import instagram.models.Post;
import instagram.repository.CommentRepo;
import instagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentSerImpl implements CommentService {
    private final CommentRepo commentRepo;
    @Override
    public void save(Long userId, Post post, Comment comment) {
        if (userId == null) {
            throw new NullComent("id is null");
        }
        if (post == null) {
            throw new NullComent("postId is null");
        }
        commentRepo.save(userId,post,comment);

    }

    @Override
    public List<Comment> getCommentsByPostId(Long id) {
        return commentRepo.getCommentsByPostId(id);
    }
}
