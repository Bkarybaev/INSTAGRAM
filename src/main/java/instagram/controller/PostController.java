package instagram.controller;

import instagram.exeptions.NullComent;
import instagram.models.*;
import instagram.repository.impl.UserInfoRepoImpl;
import instagram.repository.impl.UserRepoImpl;
import instagram.service.CommentService;
import instagram.service.LikeService;
import instagram.service.PostService;
import instagram.service.UserService;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    public User currentUser() {
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        return user;
    }

    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        deletedComment(commentId);
        return "redirect:/post/" + comment.getPost().getId();
    }

    @PostMapping("/commentSearch/{commentId}/delete")
    public String deleteCommentSearch(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        deleteComment(commentId);
        return "redirect:/post/searchUserProf/" + comment.getPost().getId();
    }

    public void deletedComment(Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            throw new NullComent("id is null");
        }

        if (comment.getUser().getId().equals(currentUser().getId())) {
            commentService.deletedComment(comment.getId());
        }
    }

    @PostMapping("/comment/{commentId}/like")
    public String commentLike(@PathVariable Long commentId) {
        Post post = postService.getPostByCommentId(commentId);
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        likeService.likeComment(commentId, user);
        return "redirect:/post/" + post.getId();
    }

    @PostMapping("/commentSearch/{commentId}/like")
    public String commentLikeSearch(@PathVariable Long commentId) {
        Post post = postService.getPostByCommentId(commentId);
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        likeService.likeComment(commentId, user);
        return "redirect:/post/searchUserProf/" + post.getId();
    }

    @PostMapping("/{postId}/comment")
    public String comment(@PathVariable Long postId, @RequestParam("commentText") String text) {
        commentariySerarch(postId, text);

        Post postById = postService.getPostById(postId);
        return "redirect:/post/" + postById.getId();
    }

    @PostMapping("/{postId}/commentSearch")
    public String commentSearch(@PathVariable("postId") Long postId, @RequestParam("text") String text) {
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
        commentService.save(user.getId(), postById, comment);
    }


    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        getAllPost(id, model);
        return "post/postDetails";
    }

    @GetMapping("/{id}/allPosts")
    public String getPostsById(@PathVariable Long id, Model model) {
        getAllPost(id, model);
        return "post/allOnePost";
    }

    private void getAllPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(post.getId());
        Collections.reverse(comments);
        model.addAttribute("post", post);
        model.addAttribute("userId", post.getUser().getId());
        model.addAttribute("comments", comments);
    }


    @GetMapping("/searchUserProf/{id}")
    public String getsSearchPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(post.getId());
        Collections.reverse(comments);
        model.addAttribute("post", post);
        model.addAttribute("currentUserId", currentUser().getId());
        model.addAttribute("userId", post.getUser().getId());
        model.addAttribute("comments", comments);
        return "post/searchPostUser";
    }

    @GetMapping("/add/{userId}")
    public String addPost(@PathVariable Long userId, Model model) {
        if (userId == null) {
            return "/main/index";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("imageUrl", new Image());
        model.addAttribute("userId", userId);
        model.addAttribute("post", new Post());
        model.addAttribute("allUsers", users);
        return "post/addPost";
    }

    @PostMapping("/savePost/{id}")
    public String savePost(@PathVariable("id") Long userId,
                           @RequestParam(value = "taggedUsers",required = false) List<String> taggedUserIds,
                           @ModelAttribute("imageUrl") Image imageUrl,
                           @ModelAttribute Post post) {
        System.out.println("Tagged Users (String): \n\n\n" + taggedUserIds);

        if (taggedUserIds != null) {
            List<Long> userIds = taggedUserIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            System.out.println("Tagged Users (Long): " + userIds);

            List<User> taggedUsers = userService.getUsersByIds(userIds);
            post.setTaggedUsers(taggedUsers);
        }

//        List<User> taggedUsers = (taggedUserIds != null) ? userService.getUsersByIds(taggedUserIds) : new ArrayList<>();
//        post.setTaggedUsers(taggedUsers);
        User currentUser = userService.getUserById(userId);
        post.setUser(currentUser);
        post.getImages().add(imageUrl);
        postService.savePost(post, currentUser.getId(), imageUrl);
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

    @GetMapping("/all")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAll();
        Collections.reverse(posts);
        model.addAttribute("posts", posts);

        return "post/allPosts";
    }


}

