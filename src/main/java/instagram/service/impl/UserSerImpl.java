package instagram.service.impl;

import instagram.exeptions.NullabelExeption;
import instagram.exeptions.TaggersUserIdsNull;
import instagram.models.Follower;
import instagram.models.User;
import instagram.models.UserInfo;
import instagram.repository.UserRepo;
import instagram.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

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
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            throw new NullabelExeption("Email or password cannot be empty");
        } else if (user.getUsername().isEmpty()) {
            throw new NullabelExeption("Username cannot be empty");
        }
        return userRepo.saveUser(user);
    }

    @Override

    public Map<User, Map<UserInfo, Follower>> userProfile(Long id) {
        return userRepo.userProfile(id);
    }

    @Override
    public void updateUserProfile(Long id, User user, UserInfo userInfo) {
        User userById = userRepo.findUserById(id);
        if (userById == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        if (user.getUsername() != null && !userById.getUsername().equalsIgnoreCase(user.getUsername())) {
            userById.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !userById.getEmail().equalsIgnoreCase(user.getEmail())) {
            userById.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !userById.getPassword().equalsIgnoreCase(user.getPassword())) {
            userById.setPassword(user.getPassword());
        }

        if (userInfo != null) {
            UserInfo existingUserInfo = userById.getUserInfo();
            if (existingUserInfo == null) {
                existingUserInfo = new UserInfo();
            }
            existingUserInfo.setBiography(userInfo.getBiography());
            existingUserInfo.setFullName(userInfo.getFullName());
            existingUserInfo.setGender(userInfo.getGender());
            existingUserInfo.setImageUrl(userInfo.getImageUrl());
            userById.setUserInfo(existingUserInfo);
        }

        userRepo.updateUser(userById);
    }


    @Override
    public User findByUsername(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullabelExeption("Username cannot be empty");
        }
        return userRepo.findByUserName(name);
    }

    @Override
    public Map<Integer, Integer> findFollowersCounts(User user) {
        return userRepo.findFollowersCounts(user);
    }

    @Override
    public List<User> search(String query) {
        if (query == null || query.isEmpty()) {
            throw new NullabelExeption("Query cannot be empty");
        }
        return userRepo.search(query);
    }

    @Override
    public void saveUserFollower(User currentUser, User profileUser) {
        userRepo.saveUserFollower(currentUser,profileUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.getAllUsers();
    }

    @Override
    public List<User> getUsersByIds(List<Long> taggedUserIds) {
        if (taggedUserIds == null || taggedUserIds.isEmpty()) {
            throw new TaggersUserIdsNull("TaggedUserIds cannot be empty");
        }
        return userRepo.getUsersByIds(taggedUserIds);
    }


}
