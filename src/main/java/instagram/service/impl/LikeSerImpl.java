package instagram.service.impl;

import instagram.exeptions.NullComent;
import instagram.exeptions.PostNull;
import instagram.exeptions.UserNotFound;
import instagram.models.Post;
import instagram.models.User;
import instagram.repository.LikeRepo;
import instagram.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeSerImpl implements LikeService {
    private final LikeRepo likeRepo;
    @Override
    public void likeComment(Long commentId, User user) {
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        if (commentId == null) {
            throw new NullComent("Comment id not found");
        }
        likeRepo.likeComment(commentId,user);
    }
}
