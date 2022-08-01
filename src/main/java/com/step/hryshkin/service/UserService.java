package com.step.hryshkin.service;

import com.step.hryshkin.model.User;

import java.util.Optional;

public interface UserService {
    String printTotalPriceForCurrentUser(String name);

    void createNewUser(User user);

    Optional<User> getUserByName(String userName);
}
