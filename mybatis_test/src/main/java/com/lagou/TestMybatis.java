package com.lagou;

import com.lagou.domain.Order;
import com.lagou.domain.User;
import com.lagou.mapper.OrderMapper;
import com.lagou.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestMybatis {
    private UserMapper userMapper;
    private OrderMapper orderMapper;

    @Before
    public void before() throws IOException {
        InputStream resourceAsStream =
                Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(resourceAsStream);
        //根据sqlSessionFactory产生session
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        userMapper = sqlSession.getMapper(UserMapper.class);
        orderMapper = sqlSession.getMapper(OrderMapper.class);
    }

    @Test
    public void testCacheAfterUpdate(){
        //第一次查询,发出sql语句，并将查询的结果放入缓存中
        User u1 = userMapper.findByIdAnnotation(1);
        System.out.println(u1);

        //第二步进行了一次更新操作
        u1.setPassword("9527");
        userMapper.updateAnnotation(u1);
        //第二次查询，由于是同一个sqlSession 自动commit，会清空缓存信息
        //则此次查询也会发出sql语句
        User u2 = userMapper.findByIdAnnotation(1);
        System.out.println(u2);
    }

    @Test
    public void testCache(){
        //第一次查询,发出sql语句，并将查询的结果放入缓存中
        User u1 = userMapper.findByIdAnnotation(1);
        System.out.println(u1);

        //第二次查询，由于是同一个sqlSession，会在缓存在查找查询结果
        //如果有，则直接从缓存中取出来，不和数据库进行交互
        User u2 = userMapper.findByIdAnnotation(1);
        System.out.println(u2);
    }



    @Test
    public void testSelectUserAndRole(){
        List<User> all = userMapper.findAllUserAndRoleAnnotation();
        all.forEach(System.out::println);
    }

    @Test
    public void testSelectUserAndOrder(){
        List<User> all = userMapper.findAllUserAndOrderAnnotation();
        all.forEach(System.out::println);
    }

    @Test
    public void testSelectOrderAndUser(){
        List<Order> all = orderMapper.findAllAnnotation();
        all.forEach(System.out::println);
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setUsername("测试数据");
        user.setPassword("123");
        user.setBirthday(LocalDateTime.now());
        userMapper.insertAnnotation(user);
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setId(8);
        user.setUsername("测试数据");
        user.setPassword("456");
        user.setBirthday(LocalDateTime.now());
        userMapper.updateAnnotation(user);
    }

    @Test
    public void testDelete(){
        userMapper.deleteAnnotation(8);
    }

    @Test
    public void testFindById(){
        User user = userMapper.findByIdAnnotation(8);
        System.out.println(user);
    }

    @Test
    public void testFindAll(){
        List<User> all = userMapper.findAllAnnotation();
        all.forEach(System.out::println);
    }

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
        /*UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> all = mapper.findAll();
        all.forEach(System.out::println);*/
        //多对多查询
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> allUserAndRole = mapper.findAllUserAndRole();
        allUserAndRole.forEach(System.out::println);
    }
}
