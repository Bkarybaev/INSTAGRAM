package instagram.controller;

import instagram.models.*;
import instagram.repository.impl.UserInfoRepoImpl;
import instagram.repository.impl.UserRepoImpl;
import instagram.service.CommentService;
import instagram.service.LikeService;
import instagram.service.PostService;
import instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    @PostMapping("/comment/{commentId}/like")
    public String commentLike(@PathVariable Long commentId) {
       Post post = postService.getPostByCommentId(commentId);
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        likeService.likeComment(commentId,user);
        return "redirect:/post/" + post.getId();
    }
    @PostMapping("/commentSearch/{commentId}/like")
    public String commentLikeSearch(@PathVariable Long commentId) {
       Post post = postService.getPostByCommentId(commentId);
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        likeService.likeComment(commentId,user);
        return "redirect:/post/searchUserProf/" + post.getId();
    }

    @PostMapping("/{postId}/comment")
    public String comment(@PathVariable Long postId,@RequestParam("commentText") String text) {
        commentariySerarch(postId, text);

        Post postById = postService.getPostById(postId);
        return "redirect:/post/" + postById.getId();
    }

    @PostMapping("/{postId}/commentSearch")
    public String commentSearch(@PathVariable("postId") Long postId,@RequestParam("text") String text) {
        commentariySerarch(postId, text);
        return "redirect:/post/searchUserProf/" + postId;
    }

    private void commentariySerarch(@PathVariable("postId") Long postId, @RequestParam("commentText") String text) {
        Comment comment = new Comment();
        comment.setCommentText(text);
        Post postById = postService.getPostById(postId);
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        commentService.save(user.getId(),postById,comment);
    }


    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
       List<Comment> comments = commentService.getCommentsByPostId(post.getId());

        model.addAttribute("post", post);
        model.addAttribute("userId", post.getUser().getId());
        model.addAttribute("comments", comments);
        return "post/postDetails";
    }
    @GetMapping("/searchUserProf/{id}")
    public String getsSearchPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(post.getId());

        model.addAttribute("post", post);
        model.addAttribute("userId", post.getUser().getId());
        model.addAttribute("comments", comments);
        return "post/searchPostUser";
    }

    @GetMapping("/add/{userId}")
    public String addPost(@PathVariable Long userId, Model model) {
        if (userId == null) {
            return "/main/index";
        }
        model.addAttribute("imageUrl", new Image());
        model.addAttribute("userId", userId);
        model.addAttribute("post", new Post());
        return "post/addPost";
    }
    @PostMapping("/savePost/{id}")
    public String savePost( @PathVariable("id") Long userId, @ModelAttribute("imageUrl") Image imageUrl,@ModelAttribute Post post ) {
        User currentUser = userService.getUserById(userId);
        postService.savePost(post,currentUser.getId(),imageUrl);
        return "redirect:/users/profile/" + currentUser.getId();
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return "redirect:/post/" + postId;
    }
    @PostMapping("/{postId}/likeSearch")
    public String likePostSearch(@PathVariable Long postId) {
        postService.likePost(postId);
        return "redirect:/post/searchUserProf/" + postId;
    }






}

