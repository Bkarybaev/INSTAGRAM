package instagram.controller;

import instagram.exeptions.NullabelExeption;
import instagram.models.*;
import instagram.repository.impl.UserRepoImpl;
import instagram.service.*;
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
    private final SmsService smsService;

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
        int unreadMessagesCount = smsService.countUnreadMessages(currentUser());
        model.addAttribute("query", "");
        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("followerSs", followerCount1);
        model.addAttribute("followerNs", followerCount2);
        model.addAttribute("posts", post);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("followersList", followersList);
        model.addAttribute("followingList", followingList);
        model.addAttribute("unreadMessagesCount", unreadMessagesCount);
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

    @PostMapping("/subscribeSearch")
    public String subscribeSearch(@RequestParam("query") String query, Model model) {
        User currentUser = currentUser();
        List<User> users = userService.search(query);


        Map<Long, Boolean> isSubscribedMap = new HashMap<>();
        for (User user : users) {
            boolean isSubscribed = userService.isUserSubscribed(currentUser, user);
            isSubscribedMap.put(user.getId(), isSubscribed);
        }

        model.addAttribute("users", users);
        model.addAttribute("isSubscribedMap", isSubscribedMap);
        model.addAttribute("query", query);
        return "userInfo/search";
    }

    @PostMapping("/{id}/subscribeSearchId")
    public String subscribeSearchId(@PathVariable Long id, @RequestParam(value = "query", required = false) String query, Model model) {
        if (query == null || query.trim().isEmpty()) {
            throw new NullabelExeption("Query cannot be empty");
        }

        User currentUser = currentUser();
        User profileUser = userService.getUserById(id);

        userService.saveUserFollower(currentUser, profileUser);

        List<User> users = userService.search(query);

        Map<Long, Boolean> isSubscribedMap = new HashMap<>();
        for (User user : users) {
            boolean isSubscribed = userService.isUserSubscribed(currentUser, user);
            isSubscribedMap.put(user.getId(), isSubscribed);
        }

        model.addAttribute("users", users);
        model.addAttribute("isSubscribedMap", isSubscribedMap);
        model.addAttribute("query", query);

        return "userInfo/search";
    }

    @PostMapping("/{userId}/subscribe")
    public String subscribe(@PathVariable Long userId) {
        User currentUser = currentUser();
        User profileUser = userService.getUserById(userId);
        userService.saveUserFollower(currentUser, profileUser);
        return "redirect:/users/searchUserProfile/" + userId;
    }

    @GetMapping("/searchUserProfile/{userProfileId}")
    @Transactional
    public String searchUserProfile(@PathVariable("userProfileId") Long userId, Model model) {
        if (userId.equals(currentUser().getId())) {
            return "redirect:/users/profile/" + userId;
        }
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
        List<User> followersList = userService.getUsersByIds(user.getFollower().getSubscribes());

        List<User> followingList = userService.getUsersByIds(user.getFollower().getSubscriptions());

        model.addAttribute("userInfoSub", profileUser);
        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("userInfoProf", userInfoProf);
        model.addAttribute("followerSs", followerCount1);
        model.addAttribute("followerNs", followerCount2);
        model.addAttribute("posts", post);
        model.addAttribute("followersList", followersList);
        model.addAttribute("followingList", followingList);

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


}
