package com.lagou;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import tk.mybatis.mapper.entity.Example;

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
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        InputStream resourceAsStream =
                Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(resourceAsStream);
        //根据sqlSessionFactory产生session
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        userMapper = sqlSession.getMapper(UserMapper.class);
        orderMapper = sqlSession.getMapper(OrderMapper.class);
    }

    @Test
    public void testBaseMapper(){
        User user = new User();
        user.setId(4);
        //(1) baseMapper基础接口
        //select接口
            //根据实体中的属性进行查询,只能有一个返回值
        User user1 = userMapper.selectOne(user);
        //查询全部结果
        List<User> users = userMapper.select(null);
        //根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
        userMapper.selectByPrimaryKey(1);
        //根据实体中的属性查询总数，查询条件使用等号
        userMapper.selectCount(user);
        //insert接口
        //保存一个实体，null值也会保存，不会使用数据库默认值
        int insert = userMapper.insert(user);
        //保存实体， null属性不会保存， 使用数据库默认值
        int i = userMapper.insertSelective(user);
        //update接口
        //根据主键更新实体全部字段,null值会被更新
        int i1 = userMapper.updateByPrimaryKey(user);
        //根据主键更新实体全部字段,null值不会被更新
        int i2 = userMapper.updateByPrimaryKeySelective(user);
        //delete接口
        // 根据实体属性作为条件进行删除，查询条件使用等号
        int delete = userMapper.delete(user);
        //根据主键字段进行删除，方法的参数必须包含完整的主键属性
        userMapper.deleteByPrimaryKey(1);
        // （2）example
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id",1);
        example.createCriteria().andLike("val", "1");
        //自定义查询
        List<User> users1 = userMapper.selectByExample(example);
    }

    @Test
    public void testPageHelper(){
        PageHelper.startPage(1,2);
        List<User> all = userMapper.findAllAnnotation();
        all.forEach(System.out::println);
        PageInfo<User> pageInfo = new PageInfo<>(all);
        System.out.println("总条数: "+pageInfo.getTotal());
        System.out.println("总页数: "+pageInfo.getPages());
        System.out.println("当前页: "+pageInfo.getPageNum());
        System.out.println("每页显示长度: "+pageInfo.getPageSize());
        System.out.println("是否第一页: "+pageInfo.isIsFirstPage());
        System.out.println("是否最后一页: "+pageInfo.isIsLastPage());

    }

    @Test
    public void testSecondLevelCacheRedis(){
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        SqlSession sqlSession3 = sqlSessionFactory.openSession();
        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
        UserMapper userMapper3 = sqlSession3.getMapper(UserMapper.class);
        User u1 = userMapper1.findByIdAnnotation(1);
        System.out.println(u1);
        sqlSession1.close();//清空一级缓存
        User user = new User();
        user.setId(1);
        user.setUsername("lisi");
        userMapper3.updateAnnotation(user);
        sqlSession3.commit();

        User u2 = userMapper2.findByIdAnnotation(1);
        System.out.println(u2);
        sqlSession2.close();
    }

    @Test
    public void testTwoCacheAfterCommit(){
        //根据 sqlSessionFactory 产生 session
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        SqlSession sqlSession3 = sqlSessionFactory.openSession();
        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
        UserMapper userMapper3 = sqlSession3.getMapper(UserMapper.class);
        // 第一次查询， 发出sql语句， 并将查询的结果放入缓存中
        User u1 = userMapper1.findByIdAnnotation(1);
        System.out.println(u1);
        sqlSession1.close();
        u1.setPassword("63666");
        userMapper3.updateAnnotation(u1);
        sqlSession3.commit();
        //第二次查询，由于上次更新操作，缓存数据已经清空(防止数据脏读)，这里必须再次发出sql
        User u2 = userMapper2.findByIdAnnotation(1);
        System.out.println(u2);
        sqlSession2.close();
    }

    @Test
    public void testTwoCache(){
        //根据 sqlSessionFactory 产生 session
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
        // 第一次查询， 发出sql语句， 并将查询的结果放入缓存中
        User u1 = userMapper1.findByIdAnnotation(1);
        System.out.println(u1);
        sqlSession1.close();
        //第二次查询，即使sqlSession1已经关闭了，这次依然不发出sql语句
        User u2 = userMapper2.findByIdAnnotation(1);
        System.out.println(u2);
        sqlSession2.close();
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
