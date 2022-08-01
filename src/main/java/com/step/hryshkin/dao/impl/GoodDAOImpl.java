package com.step.hryshkin.dao.impl;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.Good;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GoodDAOImpl implements GoodDAO {
    private static final Logger LOGGER = LogManager.getLogger(GoodDAOImpl.class);

    @Override
    public Optional<Good> getByTitle(String title) {
        Optional<Good> good = Optional.empty();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOODS WHERE TITLE = '" + title + "'")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    good = Optional.of(new Good(rs.getLong("ID"),
                            rs.getNString("TITLE"),
                            rs.getBigDecimal("PRICE")));
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at GoodDAOImpl at getByTitle" + throwable);
        }
        return good;
    }

    @Override
    public Optional<Good> getById(long id) {
        Optional<Good> good = Optional.empty();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOODS WHERE ID =" + id)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    good = Optional.of(new Good(rs.getLong("ID"),
                            rs.getNString("TITLE"),
                            rs.getBigDecimal("PRICE")));
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at GoodDAOImpl in method getById" + throwable);
        }
        return good;
    }

    @Override
    public List<Good> getAll() {
        Good good;
        List<Good> goodList = new ArrayList<>();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOODS ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    good = new Good(rs.getLong("ID"),
                            rs.getNString("TITLE"),
                            rs.getBigDecimal("PRICE"));
                    goodList.add(good);
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException in method getAll" + throwable);
        }
        return goodList;
    }

    @Override
    public List<String> getGoodListByOrderId(long id) {
        List<String> goodsInBasket = new ArrayList<>();
            try (Connection connection = Connector.createConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT g.title, g.price FROM goods AS g " +
                        "JOIN orderGoods AS b ON g.id = b.goodId " +
                        "WHERE b.orderId = '" + id + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        goodsInBasket.add(rs.getNString("TITLE") + " " + rs.getBigDecimal("PRICE") + "$");
                    }
                }
            } catch (SQLException throwable) {
                LOGGER.error("SQLException at GoodDAOImpl at getGoodBasketByUserName" + throwable);
            }
        return goodsInBasket;
    }
}
