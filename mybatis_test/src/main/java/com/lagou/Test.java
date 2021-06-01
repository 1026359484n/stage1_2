package com.lagou;

import com.lagou.domain.Order;
import com.lagou.domain.User;
import com.lagou.mapper.OrderMapper;
import com.lagou.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws IOException {
        InputStream resourceAsStream =
                Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //插入
        //User user = new User();
        /*int insert = sqlSession.insert("userMapper.add", user);
        System.out.println(insert);
        System.out.println(user);
        sqlSession.commit();*/

        //修改
        //user.setId(2);
        /*user.setUsername("update");
        int update = sqlSession.update("userMapper.update", user);
        System.out.println(update);
        sqlSession.commit();*/
        //删除
        /*int delete = sqlSession.delete("userMapper.delete", 2);
        System.out.println(delete);
        sqlSession.commit();*/
        //查询
        /*List<User> userList = sqlSession.selectList("userMapper.findAll");
        System.out.println(userList);

        sqlSession.close();*/
        //代理模式查询
        /*UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.findById(1);
        System.out.println(user);*/

        //动态sql查询
        /*UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User condition = new User();
        condition.setId(1);
        User user1 = userMapper.findByCondition(condition);
        System.out.println(user1);
        condition.setUsername("root");
        User user2 = userMapper.findByCondition(condition);
        System.out.println(user2);*/

        //动态sql循环拼接
        /*UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //List<Integer> ids = IntStream.range(1, 10).boxed().collect(Collectors.toList());
        List<Integer> ids = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        List<User> userList = userMapper.findByIds(ids);
        System.out.println(userList);*/
        //一对一映射查询
        /*OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        List<Order> all = orderMapper.findAll();
        all.forEach(System.out::println);*/

        //一对多查询
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> all = mapper.findAll();
        all.forEach(System.out::println);
    }
}
