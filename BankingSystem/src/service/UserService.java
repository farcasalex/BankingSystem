package service;

import model.entity.User;
import java.util.List;

public interface UserService {
    User login(String userName, String password);

    User create(String userName, String password, Boolean admin);

    User viewById(Long id);

    List<User> viewAll();

    Boolean delete(Long id);

    User update(User user);
}
