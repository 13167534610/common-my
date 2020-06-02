package com.common.funciton.aqmp;

import com.common.funciton.aqmp.config.QueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 16:28
 */
@Component
public class Consumer {
    Logger logger = LoggerFactory.getLogger(Consumer.class);
    @Resource(name = "rabbitAdmin")
    private RabbitAdmin rabbitAdmin;

    /**
     * 监听simpleQueue
     * @param msg 接收的消息
     */
    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                value = @Queue(value = QueueConfig.QUEUE_1, durable = "true"),
                exchange = @Exchange(value = QueueConfig.EXCHANGE_DIRECT, type = ExchangeTypes.DIRECT),
                key = "queue1"
            )
    )
    public void linstenerSimpleQueue(String msg){
        logger.info("[receive]exchange:{}, routKey:{}, msg:{}", QueueConfig.EXCHANGE_DIRECT, QueueConfig.QUEUE_1, msg);
    }
}
