package instagram.controller;

import instagram.models.User;
import instagram.repository.UserRepo;
import instagram.repository.impl.UserRepoImpl;
import instagram.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorController {
    private final UserService userService;
    private final UserRepo userRepo;

    @GetMapping("/sing-in")
    public String singIn(Model model) {
        model.addAttribute("newUser", new User());
        return "/main/index";
    }

    @GetMapping("/saveSingIn")
    public String saveSingIn(@ModelAttribute("newUser") User user, Model model, HttpSession session) {
        User user1 = userService.testEmailAndPass(user.getEmail(), user.getPassword());
        session.setAttribute("user", user1);
        if (user1 != null) {
            model.addAttribute("newUser", user1);
            return "redirect:/users/profile/" + user1.getId();
        }
        return "/main/index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("newUser", new User());
        return "/register/sing-up";
    }

    @PostMapping("/saveRegister")
    public String saveRegister(@ModelAttribute("newUser") User user,HttpSession session) {
        if (user.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")){
            User user1 = UserRepoImpl.user1;
            session.setAttribute("user", user1);
            String message = userService.saveUser(user);
            if (message.equalsIgnoreCase("success")) {
                User byEmail = userRepo.getByEmail(user.getEmail());
                return "redirect:/users/profile/" + byEmail.getId();
            }
        }
        return "/main/index";
    }

}
