package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.OrderDAO;
import com.step.hryshkin.model.Order;
import com.step.hryshkin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public String printTotalPriceForOrder(long id) {
        return orderDAO.getTotalPriceByOrderId(id).toString();
    }

    @Override
    public void createNewOrder(Order order) {
        orderDAO.createNewOrder(order);
    }

    @Override
    public Optional<Order> getLastOrder() {
        return orderDAO.getLastOrder();
    }

    @Override
    public void updateOrder(Order order) {
        orderDAO.updateOrder(order);
    }
}
