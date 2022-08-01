package com.step.hryshkin.service.impl;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.User;
import com.step.hryshkin.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public String printTotalPriceForCurrentUser(String userName) {
        Optional<User> newUser = userDAO.getUserByName(userName);
        BigDecimal totalPrice = new BigDecimal("0");
        if (newUser.isPresent()) {
            String userNameId = String.valueOf(newUser.get().getId());
            try (Connection connection = Connector.createConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT SUM (totalPrice) AS RESULT FROM orders " +
                        "WHERE USERID = '" + userNameId + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String result = rs.getNString("RESULT");
                        if (result == null) {
                            result = "0";
                        }
                        totalPrice = new BigDecimal(result);
                    }
                }
            } catch (SQLException throwable) {
                LOGGER.error("SQLException at GoodServiceImpl at printTotalPriceForCurrentUser" + throwable);
            }
        }
        return totalPrice.toString();
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
