package instagram.service;

import instagram.models.Follower;
import instagram.models.User;
import instagram.models.UserInfo;

import java.util.Map;

public interface UserService {
    User getUserById(Long id);

    User testEmailAndPass(String email, String password);

    String saveUser(User user);

    Map<User, Map<UserInfo, Follower>> userProfile(Long id);
}
