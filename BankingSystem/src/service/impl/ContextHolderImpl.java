package service.impl;

import model.entity.User;
import service.ContextHolder;

public class ContextHolderImpl implements ContextHolder {
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}
