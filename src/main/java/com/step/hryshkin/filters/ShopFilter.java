package com.step.hryshkin.filters;

import com.step.hryshkin.config.ContextInitializer;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.Order;
import com.step.hryshkin.model.OrderGood;
import com.step.hryshkin.model.User;
import com.step.hryshkin.service.GoodService;
import com.step.hryshkin.service.OrderGoodService;
import com.step.hryshkin.service.OrderService;
import com.step.hryshkin.service.UserService;
import com.step.hryshkin.service.impl.GoodServiceImpl;
import com.step.hryshkin.service.impl.OrderGoodServiceImpl;
import com.step.hryshkin.service.impl.OrderServiceImpl;
import com.step.hryshkin.service.impl.UserServiceImpl;
import com.step.hryshkin.utils.UtilsForOnlineShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebFilter(urlPatterns = {"/order_page.jsp", "/order_page_notnull.jsp"})
public class ShopFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(ShopFilter.class);
    private final UserService userService = ContextInitializer
            .getContext()
            .getBean(UserServiceImpl.class);
    private final GoodService goodService = ContextInitializer
            .getContext()
            .getBean(GoodServiceImpl.class);
    private final OrderService orderService = ContextInitializer
            .getContext()
            .getBean(OrderServiceImpl.class);
    private final OrderGoodService orderGoodService = ContextInitializer
            .getContext()
            .getBean(OrderGoodServiceImpl.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setAttribute("goods", goodService.getAll());
        checkUser((HttpServletRequest) servletRequest);
        checkFlag(servletResponse, request);
        checkForNewOrder((HttpServletRequest) servletRequest);
        try {
            filterChain.doFilter(request, servletResponse);
        } catch (IOException exception) {
            LOGGER.error("IOException in doFilter " + exception);
        } catch (ServletException exception) {
            LOGGER.error("ServletException in doFilter " + exception);
        }
    }

    private void checkUser(HttpServletRequest request) {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User(login, password);
        if (login != null) {
            if (userService.getUserByName(login).isEmpty()) {
                userService.createNewUser(user);
            }
            Optional<User> newUser = userService.getUserByName(login);
            if (newUser.isPresent()) {
                if (request.getSession().getAttribute("user") == null) {
                    UtilsForOnlineShop.setUser(request, newUser.get());
                } else if (!UtilsForOnlineShop.isUsersEquals(request)) {
                    request.getSession().invalidate();
                    UtilsForOnlineShop.setUser(request, newUser.get());
                }
            }
        }
    }

    private void checkForNewOrder(HttpServletRequest request) {
        if (request.getParameter("select") != null) {
            Long userId = ((User) request.getSession().getAttribute("user")).getId();
            long goodId = Long.parseLong(request.getParameter("select"));
            Optional<Good> currentGood = goodService.getById(goodId);
            if (currentGood.isPresent()) {
                if (request.getSession().getAttribute("order") != null) {
                    long currentOrderId = ((Order) request.getSession().getAttribute("order")).getId();
                    BigDecimal currentOrderTotalPrice =
                            ((Order) request.getSession().getAttribute("order")).getTotalPrice();
                    currentOrderTotalPrice = currentOrderTotalPrice.add(currentGood.get().getPrice());
                    Order order = new Order(currentOrderId, userId, currentOrderTotalPrice);
                    orderService.updateOrder(order);
                    UtilsForOnlineShop.setOrder(request, order);
                    OrderGood orderGood = new OrderGood(currentOrderId, goodId);
                    orderGoodService.createNewOrderGoodDAO(orderGood);
                }

                if (request.getSession().getAttribute("order") == null) {
                    Order order = new Order(userId, currentGood.get().getPrice());
                    orderService.createNewOrder(order);
                    Optional<Order> newestOrder = orderService.getLastOrder();
                    if (newestOrder.isPresent()) {
                        UtilsForOnlineShop.setOrder(request, newestOrder.get());
                        long orderId = newestOrder.get().getId();
                        OrderGood orderGood = new OrderGood(orderId, goodId);
                        orderGoodService.createNewOrderGoodDAO(orderGood);
                    }
                }
            }
        }
    }

    private void checkFlag(ServletResponse servletResponse, HttpServletRequest request) {
        if (request.getSession().getAttribute("check") == null) {
            if (request.getParameter("check") != null) {
                UtilsForOnlineShop.setCheckStatus(request, request.getParameter("check"));
            } else {
                String path = "/terms_of_use_error.jsp";
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
                try {
                    requestDispatcher.forward(request, servletResponse);
                } catch (ServletException exception) {
                    LOGGER.error("ServletException in checkFlag " + exception);
                } catch (IOException exception) {
                    LOGGER.error("IOException in checkFlag " + exception);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
