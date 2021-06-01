package com.lagou.mapper;

import com.lagou.domain.Order;

import java.util.List;

public interface OrderMapper {
    List<Order> findAll();
}
