package com.lagou.mapper;

import com.lagou.domain.Order;
import com.lagou.domain.User;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {
    List<Order> findAll();
    @Select("select * from `order`")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "orderTime",column = "order_time"),
            @Result(property = "total",column = "total"),
            @Result(property = "user",column = "uid",
                    javaType = User.class,
                    one = @One(select = "com.lagou.mapper.UserMapper.findByIdAnnotation")
            )
    })
    List<Order> findAllAnnotation();
    @Select("select * from `order` where uid=#{uid}")
    List<Order> findByUid(Integer uid);

}
