package instagram.controller;

import instagram.models.*;
import instagram.service.ImageService;
import instagram.service.PostService;
import instagram.service.UserInfoService;
import instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final UserInfoService userInfoService;

    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable("id") Long id, Model model) {
        Map<User, Map<UserInfo, Follower>> profileData = userService.userProfile(id);

        if (profileData == null) {
            return "redirect:/main/index";
        }

        User user = profileData.keySet().iterator().next();
        UserInfo userInfo = profileData.get(user).keySet().iterator().next();
//        Follower follower = profileData.get(user).get(userInfo);
        List<Post> post = postService.getPostsByUserId(user.getId());
        Map<Integer,Integer> followerCounts = userService.findFollowersCounts(user);
        int followerCount1 = followerCounts.keySet().iterator().next();
        int followerCount2 = followerCounts.values().iterator().next();

        User currentUser = userService.findByUsername(user.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("followerSs", followerCount1);
        model.addAttribute("followerNs",followerCount2);
        model.addAttribute("posts", post);
        model.addAttribute("currentUser", currentUser);
        return "userInfo/userProfile";
    }

    @GetMapping("/update-profile/{id}")
    public String updateUserProfile(@PathVariable("id") Long id, Model model) {
        Map<User, Map<UserInfo, Follower>> profileData = userService.userProfile(id);
        User user1 = profileData.keySet().iterator().next();
        UserInfo userInfo = profileData.get(user1).keySet().iterator().next();
        User user = userService.getUserById(id);
        model.addAttribute("user", user1);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("userId", user.getId());
        return "userInfo/updateProfile";
    }

    @PostMapping("/saveUpdateProfile/{id}")
    public String saveUpdatedProfile(@PathVariable("id") Long id,
                                     @ModelAttribute("user") User user,
                                     @ModelAttribute("userInfo") UserInfo userInfo,
                                     Model model) {
        User userById = userService.getUserById(id);

        try {
        String message = userInfoService.updateUserInfo(userInfo);
            userService.updateUserProfile(id, user,userInfo);
            model.addAttribute("successMessage", "Profile updated " + message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating profile!");
        }
        return "redirect:/users/profile/" + userById.getId();
    }
    @GetMapping("/searchByQuery")
    public String searchByQuery( Model model) {
        model.addAttribute("query" , "");
        return "userInfo/search";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("query") String query, Model model) {
        List<User> users = userService.search(query);
        model.addAttribute("users", users);
        return "userInfo/search";
    }
    @GetMapping("/searchUserProfile/{userProfileId}")
    public String searchUserProfile(@PathVariable("userProfileId") Long userId, Model model) {
        Map<User, Map<UserInfo, Follower>> userMapMap = userService.userProfile(userId);

        User user = userMapMap.keySet().iterator().next();
        UserInfo userInfo = userMapMap.get(user).keySet().iterator().next();

        List<Post> post = postService.getPostsByUserId(userId);

        Map<Integer, Integer> followerCounts = userService.findFollowersCounts(user);
        int followerCount1 = followerCounts.keySet().iterator().next();
        int followerCount2 = followerCounts.values().iterator().next();

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("followerSs", followerCount1);
        model.addAttribute("followerNs", followerCount2);
        model.addAttribute("posts", post);

        return "userInfo/searchUserProfile";
    }



}
