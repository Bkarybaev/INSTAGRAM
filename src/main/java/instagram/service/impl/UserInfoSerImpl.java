package instagram.service.impl;

import instagram.models.User;
import instagram.models.UserInfo;
import instagram.repository.UserInfoRepo;
import instagram.repository.UserRepo;
import instagram.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoSerImpl implements UserInfoService {
    private final UserInfoRepo userInfoRepo;
    private final UserRepo userRepo;
    @Override
    public String updateUserInfo(UserInfo userInfo) {
        return userInfoRepo.updateUserInfo(userInfo);
    }
}
