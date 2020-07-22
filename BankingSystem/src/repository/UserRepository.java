package repository;

import model.entity.User;
import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    User loadByUserName(String userName);

    User create(User user);

    User update(User user);

    boolean delete(Long id);
}
