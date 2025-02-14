package instagram.service;

import instagram.models.User;

public interface UserService {
    User getUserById(Long id);

    User testEmailAndPass(String email, String password);

    String saveUser(User user);
}
