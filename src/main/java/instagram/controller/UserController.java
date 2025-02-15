package instagram.controller;

import instagram.models.Follower;
import instagram.models.Post;
import instagram.models.User;
import instagram.models.UserInfo;
import instagram.service.PostService;
import instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;

//    @GetMapping("/profile/{id}")
//    public String getUserProfile(@PathVariable("id") Long id, Model model) {
//        User user = userService.getUserById(id);
//       Map<User, Map<UserInfo, Follower>> res = userService.userProfile(id);
//
//        if (user == null) {
//            return "redirect:/main/index";
//        }
//        model.addAttribute("user", user);
//
//        return "userInfo/userProfile";
//    }

    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable("id") Long id, Model model) {
        Map<User, Map<UserInfo, Follower>> profileData = userService.userProfile(id);

        if (profileData == null) {
            return "redirect:/main/index";
        }

        User user = profileData.keySet().iterator().next();
        UserInfo userInfo = profileData.get(user).keySet().iterator().next();
        Follower follower = profileData.get(user).get(userInfo);
        List<Post> post = postService.getPostsByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("follower", follower);
        model.addAttribute("posts",post);
        return "userInfo/userProfile";
    }








}
