package instagram.repository.impl;

import instagram.models.Follower;
import instagram.models.User;
import instagram.models.UserInfo;
import instagram.repository.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            entityManager.persist(user);
            return "success";
        }
        return "error";



    }
}
