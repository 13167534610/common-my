package com.common.funciton.aqmp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/12/6 12:39
 */
@Component
public class RabbitTemplateConfirmAndReturn implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

    private transient Logger logger = LoggerFactory.getLogger(this.getClass());
    /*@Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }*/

    /**
     * 消息确认
     * 消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        //String mqId = correlationData.getId();
        logger.info("correlationData: {}", correlationData);
        logger.info("act: {}", ack);
        logger.info("cause: {}", cause);
        /*if (ack){
            logger.info("消息{}发送至交换器成功", mqId);
        }else {
            //处理丢失的消息
            logger.info("消息{}发送至交换器失败", mqId);
        }*/
    }

    /**
     * 启动消息失败返回，比如路由不到队列时触发回调
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String msg = new String(message.getBody());
        logger.error("到队列消息主体：" + msg);
        logger.error("到队列消息返回码：" + replyCode);
        logger.error("到队列错误描述：" + replyText);
        logger.error("到队列交换器名：" + exchange);
        logger.error("到队列路由：" + routingKey);
    }
}
