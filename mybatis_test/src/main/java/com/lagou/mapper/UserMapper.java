package com.lagou.mapper;

import com.lagou.domain.User;

import java.util.List;

public interface UserMapper {
    int insert(User user);
    int update(User user);
    int delete(Integer id);
    User findById(Integer id);
    User findByCondition(User user);
    List<User> findByIds(List<Integer> ids);
    List<User> findAll();
}
