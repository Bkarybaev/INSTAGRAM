package instagram.repository;

import instagram.models.Follower;
import instagram.models.User;
import instagram.models.UserInfo;

import java.util.Map;

public interface UserRepo {
    User findUserById(Long id);

    User getByEmail(String email);

    String saveUser(User user);

    Map<User, Map<UserInfo, Follower>> userProfile(Long id);

    void updateUser(User userById);

    User findByUserName(String name);
}
