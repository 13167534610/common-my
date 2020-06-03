package com.common.funciton.aqmp;

import com.common.funciton.ItvJsonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 15:27
 */

@Component
public class RabbitTemplateProvider {




    public void sendMsg(RabbitTemplate rabbitTemplate, String exchange, String routKey, Object obj){
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        MessageProperties properties = new MessageProperties();
        properties.setAppId("myRabbitTemplate");
        properties.setClusterId("myRabbitTemplate");
        Message message = messageConverter.toMessage(obj, properties);

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routKey, message, correlationData);
    }


}
