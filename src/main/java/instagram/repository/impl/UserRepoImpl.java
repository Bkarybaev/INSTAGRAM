package instagram.repository.impl;

import instagram.models.Follower;
import instagram.models.Post;
import instagram.models.User;
import instagram.models.UserInfo;
import instagram.repository.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepoImpl implements UserRepo {
    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByEmail(String email) {
        return entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();

    }

    @Override
    public String saveUser(User user) {
        List<User> resultList = entityManager.createQuery("select u from User u where u.email = :email or u.username = :pass", User.class)
                .setParameter("email", user.getEmail())
                .setParameter("pass", user.getPassword())
                .getResultList();
        if (resultList == null || resultList.isEmpty()) {
            Follower follower = new Follower();
            UserInfo userInfo = new UserInfo();
            user.setUserInfo(userInfo);
            user.setFollower(follower);
            userInfo.setImageUrl("https://cdn.vectorstock.com/i/1000v/66/13/default-avatar-profile-icon-social-media-user-vector-49816613.jpg");
            entityManager.persist(user);
            return "success";
        }
        return "error";
    }

    @Override
    @Transactional
    public Map<User, Map<UserInfo, Follower>> userProfile(Long id) {
        User user = findUserById(id);

        List<Post> sortedPosts = user.getPosts()
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .collect(Collectors.toList());

        user.setPosts(sortedPosts);

        UserInfo userInfo = user.getUserInfo();
        Follower follower = user.getFollower();
        System.out.println(follower);
        Map<UserInfo, Follower> profileDetails = new HashMap<>();
        profileDetails.put(userInfo, follower);

        Map<User, Map<UserInfo, Follower>> userProfile = new HashMap<>();
        userProfile.put(user, profileDetails);

        return userProfile;
    }

}
