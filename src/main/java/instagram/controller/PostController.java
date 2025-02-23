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
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
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

    @PostMapping("/commentDeleted/{commentId}/delete")
    public String commentDeletedAllPost(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        Long postId = comment.getPost().getId();
        deletedComment(commentId);
        return "redirect:/post/" + postId + "/allPosts";
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
    @PostMapping("/commentAllPosts/{commentId}/like")
    public String commentLikeAllPosts(@PathVariable Long commentId) {
        Post post = postService.getPostByCommentId(commentId);
        Long postId = post.getId();
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        likeService.likeComment(commentId, user);
        return "redirect:/post/" + postId + "/allPosts";
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
    @PostMapping("/{postId}/commentAddAllPost")
    public String commentAddAllPost(@PathVariable Long postId, @RequestParam("commentText") String text) {
        commentariySerarch(postId, text);

        Post postById = postService.getPostById(postId);
        return "redirect:/post/" + postById.getId() + "/allPosts";
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
        model.addAttribute("user_Id", currentUser().getId());
        return "post/allOnePost";
    }

    private void getAllPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(post.getId());
        Collections.reverse(comments);
        model.addAttribute("post", post);
        model.addAttribute("currentUser", currentUser());
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
    public String addPost(HttpSession session, @PathVariable Long userId, Model model) {
        if (userId == null) {
            return "/main/index";
        }
        session.setAttribute("current", userId);
        model.addAttribute("imageUrl", new Image());
        model.addAttribute("userId", userId);
        model.addAttribute("post", new Post());
        model.addAttribute("search", "");
        List<Long> ids = (List<Long>) session.getAttribute("usersId");
        List<User> userList = new ArrayList<>();
        if (ids != null) {
            userList.addAll(userService.getUsersByIds(ids));

        }

        List<User> findUsers = (List<User>) session.getAttribute("findUsers");
        model.addAttribute("findUsers", findUsers);
        model.addAttribute("userList", userList);
        return "post/addPost";
    }

@PostMapping("/searchUserToAddPost")
public String searchUserToAddPost(HttpSession session, @ModelAttribute("search") String search) {
    Long id = (Long) session.getAttribute("current");
    List<User> searchResults = new ArrayList<>();

    if (search != null && !search.trim().isEmpty()) {
        searchResults = userService.search(search);
    }

    List<Long> taggedUserIds = (List<Long>) session.getAttribute("usersId");

    if (taggedUserIds != null) {
        searchResults = searchResults.stream()
                .filter(user -> !taggedUserIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    session.setAttribute("findUsers", searchResults);
    return "redirect:/post/add/" + id;
}


    @PostMapping("/addUser/{userId}")
    public String addUser(HttpSession session, @PathVariable("userId") Long userId) {
        Long id = (Long) session.getAttribute("current");
        List<Long> userIds = (List<Long>) session.getAttribute("usersId");
        if (userIds == null) {
            userIds = new ArrayList<>();
        }
        userIds.add(userId);
        session.setAttribute("usersId", userIds);
        session.removeAttribute("findUsers");
        return "redirect:/post/add/" + id;
    }

    @PostMapping("/removeTaggedUser/{userId}")
    public String removeTaggedUser(HttpSession session, @PathVariable("userId") Long userId) {
        List<Long> userIds = (List<Long>) session.getAttribute("usersId");
        Long id = (Long) session.getAttribute("current");
        userIds.remove(userId);
        session.setAttribute("usersId", userIds);
        return "redirect:/post/add/" + id;
    }

    @PostMapping("/savePost/{id}")
    public String savePost(HttpSession session, @PathVariable("id") Long userId,
                           @ModelAttribute("imageUrl") Image imageUrl,
                           @ModelAttribute Post post) {

        List<Long> userIds = (List<Long>) session.getAttribute("usersId");
        List<User> taggedUsers = null;
        if (userIds != null) {
             taggedUsers = userService.getUsersByIds(userIds);
        }


        User currentUser = userService.getUserById(userId);
        post.setUser(currentUser);
        post.getImages().add(imageUrl);
        postService.savePost(post, currentUser.getId(), imageUrl,taggedUsers);
        session.removeAttribute("usersId");
        session.removeAttribute("findUsers");
        return "redirect:/users/profile/" + currentUser.getId();
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return "redirect:/post/" + postId;
    }
    @PostMapping("/allPost/{postId}/like")
    public String likePostAll(@PathVariable Long postId) {
        postService.likePost(postId);
        return "redirect:/post/" + postId + "/allPosts";
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
        Map<User, Map<UserInfo, Follower>> userMapMap1 = userService.userProfile(currentUser().getId());
        UserInfo userInfoProf = userMapMap1.keySet().iterator().next().getUserInfo();


        model.addAttribute("userInfoProf", userInfoProf);
        model.addAttribute("posts", posts);

        return "post/allPosts";
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
            Post post = postService.getPostById(postId);
        Long nextPostId = postService.delete(postId);
        if (nextPostId == null || nextPostId == 0 || nextPostId.describeConstable().isEmpty()) {
            return "redirect:/users/profile/" + post.getUser().getId();
        }
        return "redirect:/post/" + nextPostId;
    }

    @PostMapping("/update/{id}")
    @Transactional
    public String updatePost(@PathVariable Long id,@ModelAttribute Post newpost) {
        Post post = postService.getPostById(id);
        post.setTitle(newpost.getTitle());
        post.setDescription(newpost.getDescription());
        return "redirect:/post/" + post.getId();

    }
    @PostMapping("/removeTag/{postId}/{userId}")
    @Transactional
    public String removeTaggedUser(@PathVariable Long postId, @PathVariable Long userId) {
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(userId);

        if (post != null && user != null) {
            post.getTaggedUsers().remove(user);
            postService.save(post);
        }

        return "redirect:/post/" + postId;
    }



}

