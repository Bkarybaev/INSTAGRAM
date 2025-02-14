package instagram.repository;

import instagram.models.User;

public interface UserRepo {
    User findUserById(Long id);

    User getByEmail(String email);

    String saveUser(User user);
}
