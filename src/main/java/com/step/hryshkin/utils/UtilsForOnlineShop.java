package com.step.hryshkin.utils;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.dao.impl.UserDAOImpl;
import com.step.hryshkin.model.Order;
import com.step.hryshkin.model.User;
import com.step.hryshkin.service.impl.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilsForOnlineShop {
    private static final GoodDAO goodDAO = new GoodDAOImpl();
    private static final UserDAO userDAO = new UserDAOImpl();
    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    private UtilsForOnlineShop() {
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }

    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    public static void setOrder(HttpServletRequest request, Order order) {
        request.getSession().setAttribute("order", order);
    }

    public static Order getOrder(HttpServletRequest request) {
        return (Order) request.getSession().getAttribute("order");
    }

    public static boolean isUsersEquals(HttpServletRequest request) {
        return ((User) request.getSession().getAttribute("user"))
                .getLogin().equals(request.getParameter("username"));
    }

    public static boolean isOrdersEqualsById(HttpServletRequest request, long id) {
        return ((Order) request.getSession().getAttribute("order"))
                .getId().equals(id);
    }

    public static void setCheckStatus(HttpServletRequest request, String check) {
        request.getSession().setAttribute("check", check);
    }

    public static List<String> printGoodsForCurrentUser(String name) {
        Optional<User> newUser = userDAO.getUserByName(name);
        List<String> goodsInBasket = new ArrayList<>();
        if (newUser.isPresent()) {
            String userNameId = String.valueOf(newUser.get().getId());
            try (Connection connection = Connector.createConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT g.title, o.totalPrice FROM orders AS o " +
                        "JOIN orderGoods AS b ON o.id = b.orderId " +
                        "JOIN goods AS g ON b.goodId = g.id " +
                        "WHERE o.userId = '" + userNameId + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        goodsInBasket.add(rs.getNString("TITLE") + " " + rs.getBigDecimal("TOTALPRICE") + "$");
                    }
                }
            } catch (SQLException throwable) {
                LOGGER.error("SQLException at UtilsForOnlineShop at printGoodsForCurrentUser " + throwable);
            }
        }
        return goodsInBasket;
    }

    public static List<String> printGoodsForCurrentOrder(long id) {
        return goodDAO.getGoodListByOrderId(id);
    }

    public static void stopShopping(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
