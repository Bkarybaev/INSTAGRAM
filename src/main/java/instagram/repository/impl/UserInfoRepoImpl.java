package instagram.repository.impl;

import instagram.models.UserInfo;
import instagram.repository.UserInfoRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserInfoRepoImpl implements UserInfoRepo {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String updateUserInfo(UserInfo userInfo1) {
        UserInfo userInfo = entityManager.find(UserInfo.class, userInfo1.getId());
        entityManager.merge(userInfo);
        return "success";
    }

}
