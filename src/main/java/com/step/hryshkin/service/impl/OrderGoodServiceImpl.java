package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.OrderGoodDAO;
import com.step.hryshkin.model.OrderGood;
import com.step.hryshkin.service.OrderGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderGoodServiceImpl implements OrderGoodService {

    private final OrderGoodDAO orderGoodDAO;

    @Autowired
    public OrderGoodServiceImpl(OrderGoodDAO orderGoodDAO) {
        this.orderGoodDAO = orderGoodDAO;
    }

    @Override
    public void createNewOrderGoodDAO(OrderGood orderGood) {
        orderGoodDAO.createNewOrderGoodDAO(orderGood);
    }
}
