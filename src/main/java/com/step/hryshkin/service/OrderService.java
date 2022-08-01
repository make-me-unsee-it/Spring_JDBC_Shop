package com.step.hryshkin.service;

import com.step.hryshkin.model.Order;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderService {

    String printTotalPriceForOrder(long id);

    void createNewOrder(Order order);

    Optional<Order> getLastOrder();

    void updateOrder(Order order);

    BigDecimal getTotalPriceByOrderId(long id);
}
