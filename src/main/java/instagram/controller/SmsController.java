package instagram.controller;

import instagram.models.Follower;
import instagram.models.Sms;
import instagram.models.User;
import instagram.models.UserInfo;
import instagram.repository.impl.UserRepoImpl;
import instagram.service.SmsService;
import instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final UserService userService;
    private final SmsService smsService;
    public User currentUser() {
        User user = userService.getUserById(UserRepoImpl.user.getId());
        if (user == null) {
            user = userService.getUserById(UserRepoImpl.user1.getId());
        }
        return user;
    }

    @GetMapping("/getUsers")
    public String getUsers(Model model) {
        int unreadMessagesCount = smsService.countUnreadMessages(currentUser());
        List<User> users = userService.getAllUsers();
        Map<User, Map<UserInfo, Follower>> userMapMap1 = userService.userProfile(currentUser().getId());
        UserInfo userInfoProf = userMapMap1.keySet().iterator().next().getUserInfo();
        users.remove(currentUser());
        Map<Long, Integer> unreadMessagesPerUser = smsService.countUnreadMessagesPerUser(currentUser());
        model.addAttribute("userInfoProf",userInfoProf);
        model.addAttribute("users", users);
        model.addAttribute("unreadMessagesCount", unreadMessagesCount);
        model.addAttribute("unreadMessagesPerUser", unreadMessagesPerUser);
        return "sms/users";
    }
    @GetMapping("/chat/{id}")
    public String chat(@PathVariable Long id, Model model) {
        int unreadMessagesCount = smsService.countUnreadMessages(currentUser());
        Map<Long, Integer> unreadMessagesPerUser = smsService.countUnreadMessagesPerUser(currentUser());
        User chatUser = userService.getUserById(id);
        List<Sms> messages = smsService.getMessages(currentUser(), chatUser);
        smsService.markMessagesAsRead(currentUser(), chatUser);
        List<User> users = userService.getAllUsers();
        users.remove(currentUser());
        Map<User, Map<UserInfo, Follower>> userMapMap1 = userService.userProfile(currentUser().getId());
        UserInfo userInfoProf = userMapMap1.keySet().iterator().next().getUserInfo();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (Sms message : messages) {
            if (message.getTime().toLocalDate().isEqual(LocalDate.now())) {
                message.setFormattedTime(message.getTime().format(timeFormatter));
            } else {
                message.setFormattedTime(message.getTime().format(dateTimeFormatter));
            }
        }



        model.addAttribute("userInfoProf", userInfoProf);
        model.addAttribute("chatUser", chatUser);
        model.addAttribute("messages", messages);
        model.addAttribute("users", users);
        model.addAttribute("unreadMessagesCount", unreadMessagesCount);
        model.addAttribute("unreadMessagesPerUser", unreadMessagesPerUser);

        return "sms/chat";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam Long receiverId, @RequestParam String message) {
        User sender = currentUser();
        User receiver = userService.getUserById(receiverId);

        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found!");
        }

        if (sender == null) {
            throw new IllegalArgumentException("Sender not found!");
        }


        smsService.sendSms(sender, receiver, message);

        return "redirect:/sms/chat/" + receiverId;
    }

}
