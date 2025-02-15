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
        if (userInfo != null) {
            if (!userInfo.getImageUrl().equalsIgnoreCase(userInfo1.getImageUrl())) {
                userInfo.setImageUrl(userInfo1.getImageUrl());
            }
            if (userInfo.getGender() != userInfo1.getGender()) {
                userInfo.setGender(userInfo1.getGender());
            }
            if (!userInfo.getBiography().equalsIgnoreCase(userInfo1.getBiography())) {
                userInfo.setBiography(userInfo1.getBiography());
            }
        }
        return "success";
    }

}
