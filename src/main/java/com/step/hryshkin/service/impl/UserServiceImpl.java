package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.User;
import com.step.hryshkin.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void createNewUser(User user) {
        userDAO.createNewUser(user);
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        return userDAO.getUserByName(userName);
    }
}
