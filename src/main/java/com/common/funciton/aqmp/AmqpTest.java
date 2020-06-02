package com.common.funciton.aqmp;

import com.common.funciton.aqmp.config.QueueConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 17:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpTest {

    @Autowired
    private RabbitTemplateProvider rabbitTemplateProvider;

    @Test
    public void test1(){
        System.out.println("=====================================测试Direct==========================================");
        String msg = "RabbitMQ简单队列测试";
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplateProvider.sendMsg(QueueConfig.EXCHANGE_DIRECT, QueueConfig.QUEUE_1, msg, correlationData);
    }

}
