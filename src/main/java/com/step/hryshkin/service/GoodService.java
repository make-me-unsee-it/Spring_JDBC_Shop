package com.step.hryshkin.service;

import com.step.hryshkin.model.Good;

import java.util.List;
import java.util.Optional;

public interface GoodService {
    List<String> getGoodListByOrderId(long id);

    List<Good> getAll();

    Optional<Good> getById(long id);

    Optional<Good> getByTitle(String title);
}
