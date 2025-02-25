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

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepoImpl implements UserRepo {
    @PersistenceContext
    private final EntityManager entityManager;
    public static User user = null;
    public static User user1 = null;


    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByEmail(String email) {

        user = entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return user;
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
            follower.setUser(user);
            userInfo.setUser(user);
            userInfo.setImageUrl("https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3467.jpg");
            entityManager.persist(user);
            user1 = entityManager.createQuery("select u from User u where u.email = :email",User.class)
                    .setParameter("email",user.getEmail())
                    .getSingleResult();
            return "success";
        }
        return "error";
    }

    @Override
    @Transactional
    public Map<User, Map<UserInfo, Follower>> userProfile(Long id) {
        User user = findUserById(id);

        List<Post> sortedPosts = user.getPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt, Comparator.reverseOrder()))
                .collect(Collectors.toList());


        user.setPosts(sortedPosts);

        UserInfo userInfo = user.getUserInfo();
        Follower follower = user.getFollower();

        Map<UserInfo, Follower> profileDetails = new HashMap<>();
        profileDetails.put(userInfo, follower);

        Map<User, Map<UserInfo, Follower>> userProfile = new HashMap<>();
        userProfile.put(user, profileDetails);

        return userProfile;
    }

    @Override
    public void updateUser(User userById) {
        entityManager.merge(userById);
    }


    @Override
    public User findByUserName(String name) {
        return entityManager.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", name).getSingleResult();
    }

    @Override
    public Map<Integer, Integer> findFollowersCounts(User user) {
        User user1 = entityManager.find(User.class, user.getId());
        int subscribes = (user1.getFollower().getSubscribes() != null) ? user1.getFollower().getSubscribes().size() : 0;
        int subscriptions = (user1.getFollower().getSubscriptions() != null) ? user1.getFollower().getSubscriptions().size() : 0;

        return Map.of(subscribes, subscriptions);
    }

    @Override
    public List<User> search(String query) {
        return entityManager.createQuery("select u from User u where u.username ilike :query or u.userInfo.fullName ilike :query", User.class)
                .setParameter("query", "%"+query+"%")
                .getResultList();
    }

    @Override
    @Transactional
    public void saveUserFollower(User currentUser, User profileUser) {

            currentUser = entityManager.find(User.class, currentUser.getId());
            profileUser = entityManager.find(User.class, profileUser.getId());

            List<Long> subscribes = profileUser.getFollower().getSubscribes();
            List<Long> subscriptions = currentUser.getFollower().getSubscriptions();

            boolean isSubscribed = subscribes.contains(currentUser.getId());

            if (!isSubscribed) {
                subscribes.add(currentUser.getId());
                subscriptions.add(profileUser.getId());
            } else {
                subscribes.remove(currentUser.getId());
                subscriptions.remove(profileUser.getId());
            }
            entityManager.merge(profileUser);
            entityManager.merge(currentUser);
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public List<User> getUsersByIds(List<Long> taggedUserIds) {
        List<User> users = new ArrayList<>();
        taggedUserIds.forEach(userId -> users.add(entityManager.find(User.class, userId)));
         return users;
    }

}
