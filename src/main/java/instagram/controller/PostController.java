package instagram.controller;

import instagram.models.Image;
import instagram.models.Like;
import instagram.models.Post;
import instagram.models.User;
import instagram.service.PostService;
import instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final UserService userService;
    private final PostService postService;


    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return "/main/index";
        }
        model.addAttribute("post", post);
        model.addAttribute("userId", post.getUser().getId());
        return "post/postDetails";
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
//        imageUrl.setUser(userService.getUserById(currentUser.getId()));
//        imageUrl.setPost(post);
//        post.setImages(new ArrayList<>());
//        post.getImages().add(imageUrl);

        postService.savePost(post,currentUser.getId(),imageUrl);
        return "redirect:/users/profile/" + currentUser.getId();
    }


}

