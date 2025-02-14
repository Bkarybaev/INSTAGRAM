package instagram.service.impl;

import instagram.exeptions.NullabelExeption;
import instagram.models.User;
import instagram.repository.UserRepo;
import instagram.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSerImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    public User getUserById(Long id) {
        return userRepo.findUserById(id);
    }

    @Override
    public User testEmailAndPass(String email, String password) {
        User user = userRepo.getByEmail(email);
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public String saveUser(User user) {
//        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
//            throw new NullabelExeption("Email or password cannot be empty");
//        } else if (user.getUsername().isEmpty()) {
//            throw new NullabelExeption("Username cannot be empty");
//        }
        return userRepo.saveUser(user);
    }


}
