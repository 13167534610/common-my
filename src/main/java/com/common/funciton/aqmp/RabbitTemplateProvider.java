package com.common.funciton.aqmp;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 15:27
 */

@Component
public class RabbitTemplateProvider {

    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;


    public void sendMsg(String exchange, String queue, Object obj, CorrelationData correlationData){
        String str = "简单队列测试";
        System.out.println(str);
        rabbitTemplate.convertAndSend(exchange, queue, obj, correlationData);
        System.out.println("exchange: " + rabbitTemplate.getExchange());


    }

}
