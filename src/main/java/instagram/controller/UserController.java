package instagram.controller;

import instagram.models.*;
import instagram.repository.impl.UserRepoImpl;
import instagram.service.ImageService;
import instagram.service.PostService;
import instagram.service.UserInfoService;
import instagram.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final UserInfoService userInfoService;

    public User currentUser() {
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        return user;
    }

    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable("id") Long id, Model model) {
        Map<User, Map<UserInfo, Follower>> profileData = userService.userProfile(id);
        if (profileData == null) {
            return "redirect:/main/index";
        }

        User user = profileData.keySet().iterator().next();
        UserInfo userInfo = profileData.get(user).keySet().iterator().next();

        List<Post> post = postService.getPostsByUserId(user.getId());
        Collections.reverse(post);

        Map<Integer, Integer> followerCounts = userService.findFollowersCounts(user);
        int followerCount1 = followerCounts.keySet().iterator().next();
        int followerCount2 = followerCounts.values().iterator().next();

        User currentUser = userService.findByUsername(user.getUsername());

        List<User> followersList = userService.getUsersByIds(user.getFollower().getSubscribes());

        List<User> followingList = userService.getUsersByIds(user.getFollower().getSubscriptions());

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("followerSs", followerCount1); // Followers саны
        model.addAttribute("followerNs", followerCount2); // Following саны
        model.addAttribute("posts", post);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("followersList", followersList); // Followers тизмеси
        model.addAttribute("followingList", followingList); // Following тизмеси

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
            userService.updateUserProfile(id, user, userInfo);
            model.addAttribute("successMessage", "Profile updated " + message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating profile!");
        }
        return "redirect:/users/profile/" + userById.getId();
    }

    @GetMapping("/searchByQuery")
    public String searchByQuery(Model model) {
        model.addAttribute("query", "");
        return "userInfo/search";
    }
private String queryP = null;
    @GetMapping("/search")
    public String search(@ModelAttribute("query") String query, Model model) {
        List<User> users = userService.search(query);
        queryP = query;
        List<Long> subscribes = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            subscribes = users.get(i).getFollower().getSubscribes();
        }
        boolean isSubscribed = false;
        for (Long id : subscribes) {
            if (currentUser().getId().equals(id)) {
                isSubscribed = true;
                break;
            }
        }

        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("users", users);
        return "userInfo/search";
    }

    @GetMapping("/searchUserProfile/{userProfileId}")
    @Transactional
    public String searchUserProfile(@PathVariable("userProfileId") Long userId, Model model) {
        Map<User, Map<UserInfo, Follower>> userMapMap = userService.userProfile(userId);

        User user = userMapMap.keySet().iterator().next();
        UserInfo userInfo = userMapMap.get(user).keySet().iterator().next();

        List<Post> post = postService.getPostsByUserId(userId);
        Collections.reverse(post);

        Map<Integer, Integer> followerCounts = userService.findFollowersCounts(user);
        int followerCount1 = followerCounts.keySet().iterator().next();
        int followerCount2 = followerCounts.values().iterator().next();

        User currentUser = userService.getUserById(UserRepoImpl.user.getId());
        if (currentUser == null) {
            currentUser = userService.getUserById(UserRepoImpl.user1.getId());
        }
        User profileUser = userService.getUserById(userId);
        Map<User, Map<UserInfo, Follower>> userMapMap1 = userService.userProfile(currentUser.getId());
        UserInfo userInfoProf = userMapMap1.keySet().iterator().next().getUserInfo();

        List<Long> subscribes = profileUser.getFollower().getSubscribes();
        boolean isSubscribed = false;
        for (Long id : subscribes) {
            if (currentUser.getId().equals(id)) {
                isSubscribed = true;
                break;
            }
        }

        model.addAttribute("userInfoSub", profileUser);
        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("userInfoProf", userInfoProf);
        model.addAttribute("followerSs", followerCount1);
        model.addAttribute("followerNs", followerCount2);
        model.addAttribute("posts", post);

        return "userInfo/searchUserProfile";
    }


    @GetMapping("/myProfile")
    public String myProfile() {
        Long id = UserRepoImpl.user.getId();
        if (id == null) {
            id = UserRepoImpl.user1.getId();
        }
        return "redirect:/users/profile/" + id;
    }

    @PostMapping("/{userId}/subscribe")
    public String subscribe(@PathVariable Long userId) {
        User currentUser = currentUser();
        User profileUser = userService.getUserById(userId);
        userService.saveUserFollower(currentUser, profileUser);
        return "redirect:/users/searchUserProfile/" + userId;
    }

    @PostMapping("/{userId}/subscribeSearch")
    public String subscribeSearch(@PathVariable Long userId, Model model) {
        User currentUser = currentUser();
        User profileUser = userService.getUserById(userId);
        userService.saveUserFollower(currentUser, profileUser);
        List<User> users = userService.search(queryP);
        Map<Long, Boolean> isSubscribedMap = new HashMap<>();
        for (User user : users) {
            boolean isSubscribed = userService.isUserSubscribed(currentUser, user);
            isSubscribedMap.put(user.getId(), isSubscribed);
        }
        model.addAttribute("users", users);
        model.addAttribute("isSubscribedMap", isSubscribedMap);

        return "userInfo/search";
    }






}
