package com.common.funciton.aqmp.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 15:29
 */
@Component
public class AmqpConfig {
    private static final String RABBIT_MQ_ADDRESS = "127.0.0.1:5672";
    private static final String USER_NAME = "wangqiang"; //账户
    private static final String PASSWORD = "123456"; //密码
    private static final String VIRTUAL_HOST = "/mytest"; //操作目录
    @Bean(value = "myConnectionFactory")
    public ConnectionFactory myConnectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses(RABBIT_MQ_ADDRESS);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);//确认回调
        factory.setPublisherReturns(true);
        factory.setChannelCacheSize(20);
        factory.setChannelCheckoutTimeout(10000);
        return factory;
    }

    @Bean("myRabbitTemplate")
    public RabbitTemplate myRabbitTemplate(@Qualifier("myConnectionFactory") ConnectionFactory myConnectionFactory) throws IOException, TimeoutException {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(myConnectionFactory);
        //使用单独的发送连接，避免生产者由于各种原因阻塞而导致消费者同样阻塞
        rabbitTemplate.setUsePublisherConnection(true);
        //实现消息发送到RabbitMQ交换器的回调确认，到达交换器ack=true否则ack=false
        rabbitTemplate.setConfirmCallback(new RabbitTemplateConfirmAndReturn());
        //当mandatory标志位设置为true时，如果exchange路由不到queue存储消息，那么broker会调用basic.return方法将消息返还给生产者
        //当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
        rabbitTemplate.setMandatory(true);
        //实现消息发送到queue的回调确认，成功发送不回调，发送失败回调（mandatory=true）
        rabbitTemplate.setReturnCallback(new RabbitTemplateConfirmAndReturn());
        return rabbitTemplate;
    }

    @Bean("myRabbitAdmin")
    public RabbitAdmin myRabbitAdmin(@Qualifier("myConnectionFactory") ConnectionFactory myConnectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(myConnectionFactory);
        return rabbitAdmin;
    }
}
