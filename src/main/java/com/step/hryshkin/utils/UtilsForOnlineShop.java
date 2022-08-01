package com.step.hryshkin.utils;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.model.Order;
import com.step.hryshkin.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UtilsForOnlineShop {
    private static final GoodDAO goodDAO = new GoodDAOImpl();

    private UtilsForOnlineShop() {
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }

    public static void setOrder(HttpServletRequest request, Order order) {
        request.getSession().setAttribute("order", order);
    }

    public static boolean isUsersEquals(HttpServletRequest request) {
        return ((User) request.getSession().getAttribute("user"))
                .getLogin().equals(request.getParameter("username"));
    }

    public static void setCheckStatus(HttpServletRequest request, String check) {
        request.getSession().setAttribute("check", check);
    }

    public static List<String> printGoodsForCurrentOrder(long id) {
        return goodDAO.getGoodListByOrderId(id);
    }

    public static void stopShopping(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
