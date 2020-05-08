package com.common.dataconfig.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @description: 数据源配置信息
 * @author: wangqiang
 * @create: 2020-04-03 11:36:33
 */
@Configuration
public class DateSourceConfig {
    @Bean(name = "dataSourceGf")
    public DataSource dataSource1(@Value("${spring.datasource-gf.username}") String username,
                                  @Value("${spring.datasource-gf.password}") String password,
                                  @Value("${spring.datasource-gf.jdbc-url}") String url,
                                  @Value("${spring.datasource-gf.driver-class-name}") String driverClassName) {
        return createDataSource(username, password, url, driverClassName);
    }

    @Bean(name = "dataSourceXshtest")
    public DataSource dataSource2(@Value("${spring.datasource-xsh.username}") String username,
                                  @Value("${spring.datasource-xsh.password}") String password,
                                  @Value("${spring.datasource-xsh.jdbc-url}") String url,
                                  @Value("${spring.datasource-xsh.driver-class-name}") String driverClassName) {
        return createDataSource(username, password, url, driverClassName);
    }

    //使用阿里巴巴连接池创建数据源
    private DataSource createDataSource(String username, String password, String url, String driverClassName) {
        DruidDataSource aliDataSource = new DruidDataSource();
        aliDataSource.setUrl(url);  //数据库连接地址
        aliDataSource.setUsername(username);    //用户名
        aliDataSource.setPassword(password);    //密码
        aliDataSource.setDriverClassName(driverClassName);  //驱动类名
        aliDataSource.setInitialSize(5);    //初始化连接数
        aliDataSource.setMaxActive(20);     //最大连接数
        aliDataSource.setMinIdle(2);        //最小连接数
        aliDataSource.setPoolPreparedStatements(true);  //开启PSChach 对使用游标数据库性能有很大提升
        aliDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);//指定PSChach的大小
        aliDataSource.setMaxWait(5000);//获取连接最大等待时间，默认使用公平锁 会降低并发效率
        aliDataSource.setUseUnfairLock(true);//使用非公平锁
        aliDataSource.setValidationQuery("select 1 from dual");//验证查询语句
        aliDataSource.setTestWhileIdle(true);//空闲时间大于TimeBetweenEvictionRunsMillis会执行验证sql检查连接是否有效
        aliDataSource.setTimeBetweenEvictionRunsMillis(300000);//检测需要关闭的空闲连接周期，
        return aliDataSource;
    }

}
