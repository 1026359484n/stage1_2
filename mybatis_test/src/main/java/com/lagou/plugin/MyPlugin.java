package com.lagou.plugin;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({//可以定义多个@Signature对多个地方拦截，都用这个拦截器
        @Signature(type = StatementHandler.class,//拦截的接口
                method = "prepare",//这个接口内的方法名
                args = {Connection.class,Integer.class}//拦截的方法的入参，按顺序写，方法重载需要通过方法名和入参确定唯一
        )
})
public class MyPlugin implements Interceptor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 每次执行操作的时候，都会进入这个拦截器的方法内
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //增强逻辑
        System.out.println("对方法进行了增强。。。。");
        //执行原方法
        return invocation.proceed();
    }

    /**
     * 为了把这个拦截器生成一个代理放到拦截器链中
     * @description 包装目标对象 为目标对象创建代理对象
     * @param target 要拦截的对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        System.out.println("将要包装的对象："+target);
        return Plugin.wrap(target,this);
    }

    /**
     * 获取配置文件的属性
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置的初始化参数: " + properties);
    }
}
