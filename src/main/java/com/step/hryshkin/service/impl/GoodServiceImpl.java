package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoodServiceImpl implements GoodService {

    private final GoodDAO goodDAO;

    @Autowired
    public GoodServiceImpl(GoodDAO goodDAO) {
        this.goodDAO = goodDAO;
    }

    @Override
    public List<String> getGoodListByOrderId(long id) {
        return goodDAO.getGoodListByOrderId(id);
    }

    @Override
    public List<Good> getAll() {
        return goodDAO.getAll();
    }

    @Override
    public Optional<Good> getById(long id) {
        return goodDAO.getById(id);
    }

}
