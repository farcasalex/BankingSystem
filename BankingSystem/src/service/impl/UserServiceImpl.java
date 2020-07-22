package service.impl;

import model.entity.User;
import repository.UserRepository;
import service.UserService;

import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String userName, String password) {
        User user = userRepository.loadByUserName(userName);
        if(user != null) {
            if(user.getPassword().equals(password)) {

                return user;
            } else {
                LOGGER.warning("Wrong password for user " + userName);
            }
        } else {
            LOGGER.warning("User with username: " + userName + " was not found");
        }
        return null;
    }

    @Override
    public User create(String userName, String password, Boolean admin) {
        User user = new User(userName, password, admin);
        return userRepository.create(user);
    }

    @Override
    public User viewById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> viewAll() {
        return userRepository.findAll();
    }

    @Override
    public Boolean delete(Long id) {
        return userRepository.delete(id);
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }
}
