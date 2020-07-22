package service;

import model.entity.User;

public interface ContextHolder {
    void setCurrentUser(User user);

    User getCurrentUser();
}
