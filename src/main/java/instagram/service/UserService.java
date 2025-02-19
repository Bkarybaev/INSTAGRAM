package instagram.service;

import instagram.models.Follower;
import instagram.models.User;
import instagram.models.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUserById(Long id);

    User testEmailAndPass(String email, String password);

    String saveUser(User user);

    Map<User, Map<UserInfo, Follower>> userProfile(Long id);

    void updateUserProfile(Long id, User user, UserInfo userInfo);

    User findByUsername(String name);

    Map<Integer, Integer> findFollowersCounts(User user);

    List<User> search(String query);

    void saveUserFollower(User currentUser, User profileUser);
}
