package com.lagou.mapper;

import com.lagou.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    int insert(User user);
    int update(User user);
    int delete(Integer id);
    User findById(Integer id);
    User findByCondition(User user);
    List<User> findByIds(List<Integer> ids);
    List<User> findAll();
    List<User> findAllUserAndRole();

    @Insert("insert into user values(#{id},#{username},#{password})")
    int insertAnnotation(User user);
    @Update("update user set username=#{username},password=#{password},birthday=#{birthday} where id=#{id}")
    int updateAnnotation(User user);
    @Delete("delete from user where id=#{id}")
    int deleteAnnotation(Integer id);
    @Select("select * from user where id = #{id}")
    User findByIdAnnotation(Integer id);
    @Select("select * from user")
    List<User> findAllAnnotation();
    @Select("select * from user")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "birthday", column = "birthday"),
            @Result(property = "orderList", column = "id",
                    javaType = List.class,
                    many = @Many(select="com.lagou.mapper.OrderMapper.findByUid")
            )
    })
    List<User> findAllUserAndOrderAnnotation();

    @Select("select * from user")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "birthday", column = "birthday"),
            @Result(property = "orderList", column = "id",
                    javaType = List.class,
                    many = @Many(select="com.lagou.mapper.OrderMapper.findByUid")
            ),
            @Result(property = "roleList", column = "id",
                    javaType = List.class,
                    many = @Many(select="com.lagou.mapper.RoleMapper.findByUid")
            )
    })
    List<User> findAllUserAndRoleAnnotation();


}
