package com.step.hryshkin.utils;

import com.step.hryshkin.model.Order;
import com.step.hryshkin.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UtilsForOnlineShop {

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

    public static void setGoodListForCurrentOrder(HttpServletRequest request, List<String> goodsForCurrentOrder) {
        request.getSession().setAttribute("goodListForCurrentOrder", goodsForCurrentOrder);
    }

    public static void stopShopping(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
