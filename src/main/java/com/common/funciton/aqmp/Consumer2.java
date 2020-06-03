package com.common.funciton.aqmp;

import com.common.funciton.ItvJsonUtil;
import com.common.funciton.SignUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 16:28
 */
@Component
public class Consumer2 {
    Logger logger = LoggerFactory.getLogger(Consumer2.class);

    @RabbitHandler
    @RabbitListener(queues = "queueWork")
    public void linstenerQueueWork(Channel channel, Message message) throws IOException {
        try {
            channel.basicQos(1);
            Thread.sleep(5000);
            logger.info("==========================消费者2监听queueWork队列==================================");
            Object msgObj = SignUtil.byte2Obj(message.getBody());//消息内容
            MessageProperties properties = message.getMessageProperties();//消息属性
            logger.info("[receive2_queueWork]content:{}", msgObj);//消息内容
            logger.info("[receive2_queueWork]exchange:{}", properties.getReceivedExchange());//交换器
            logger.info("[receive2_queueWork]consumerQueue:{}", properties.getConsumerQueue());//队列
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            //multiple 是否处理所有消息， requeue 是否放回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }

    }

    /**
     * 监听queue1
     * @param message 接收的消息
     *        监听方法中的接收参数也可以是String类型，表示直接接收消息内容
     */
    @RabbitHandler
    /*@RabbitListener(
            bindings = @QueueBinding(
                value = @Queue(value = QueueConfig.QUEUE_1, durable = "true"),
                exchange = @Exchange(value = QueueConfig.EXCHANGE_DIRECT, type = ExchangeTypes.DIRECT),
                key = "queue1"
            )
    )*/
    @RabbitListener(queues = "queue1")
    public void linstenerQueue1(Channel channel, String str, Message message, @Headers Map<String, Object> map){
        logger.info("[receive2_queue1]channel: {}", channel);
        logger.info("[receive2_queue1]str: {}", str);
        logger.info("[receive2_queue1]map: {}", ItvJsonUtil.toJson(map));
        logger.info("==========================消费者2监听queue1队列==================================");
        Object msgObj = SignUtil.byte2Obj(message.getBody());//消息内容
        MessageProperties properties = message.getMessageProperties();//消息属性
        logger.info("[receive2_queue1]content:{}", msgObj);//消息内容
        logger.info("[receive2_queue1]exchange:{}", properties.getReceivedExchange());//交换器
        logger.info("[receive2_queue1]consumerQueue:{}", properties.getConsumerQueue());//队列
        logger.info("[receive2_queue1]appId:{}", properties.getAppId());//对接应用编号
        logger.info("[receive2_queue1]clusterId:{}", properties.getClusterId());//客户编号
        logger.info("[receive2_queue1]consumerTag:{}", properties.getConsumerTag());
        logger.info("[receive2_queue1]contentEncoding:{}", properties.getContentEncoding());//消息编码
        logger.info("[receive2_queue1]contentLength:{}", properties.getContentLength());
        logger.info("[receive2_queue1]contentType:{}", properties.getContentType());//消息类型，根据消息转换器和消息体而定，自设无用
        logger.info("[receive2_queue1]correlationId:{}", properties.getCorrelationId());
        logger.info("[receive2_queue1]delay:{}", properties.getDelay());
        logger.info("[receive2_queue1]deliveryMode:{}", properties.getDeliveryMode());
        logger.info("[receive2_queue1]deliveryTag:{}", properties.getDeliveryTag());
        logger.info("[receive2_queue1]expiration:{}", properties.getExpiration());
        logger.info("[receive2_queue1]headers:{}", ItvJsonUtil.toJson(properties.getHeaders()));
    }


    /**
     * 监听queue1
     * @param message 接收的消息
     *        监听方法中的接收参数也可以是String类型，表示直接接收消息内容
     */
    @RabbitHandler
    /*@RabbitListener(
            bindings = @QueueBinding(
                value = @Queue(value = QueueConfig.QUEUE_1, durable = "true"),
                exchange = @Exchange(value = QueueConfig.EXCHANGE_DIRECT, type = ExchangeTypes.DIRECT),
                key = "queue1"
            )
    )*/
    @RabbitListener(queues = "queue2")
    public void linstenerQueue2(Message message){
        logger.info("==========================消费者2监听queue2队列==================================");
        Object msgObj = SignUtil.byte2Obj(message.getBody());//消息内容
        MessageProperties properties = message.getMessageProperties();//消息属性
        logger.info("[receive2_queue2]content:{}", msgObj);//消息内容
        logger.info("[receive2_queue2]exchange:{}", properties.getReceivedExchange());//交换器
        logger.info("[receive2_queue2]consumerQueue:{}", properties.getConsumerQueue());//队列
        logger.info("[receive2_queue2]appId:{}", properties.getAppId());//对接应用编号
        logger.info("[receive2_queue2]clusterId:{}", properties.getClusterId());//客户编号
        logger.info("[receive2_queue2]consumerTag:{}", properties.getConsumerTag());
        logger.info("[receive2_queue2]contentEncoding:{}", properties.getContentEncoding());//消息编码
        logger.info("[receive2_queue2]contentLength:{}", properties.getContentLength());
        logger.info("[receive2_queue2]contentType:{}", properties.getContentType());//消息类型，根据消息转换器和消息体而定，自设无用
        logger.info("[receive2_queue2]correlationId:{}", properties.getCorrelationId());
        logger.info("[receive2_queue2]delay:{}", properties.getDelay());
        logger.info("[receive2_queue2]deliveryMode:{}", properties.getDeliveryMode());
        logger.info("[receive2_queue2]deliveryTag:{}", properties.getDeliveryTag());
        logger.info("[receive2_queue2]expiration:{}", properties.getExpiration());
        logger.info("[receive2_queue2]headers:{}", ItvJsonUtil.toJson(properties.getHeaders()));
    }
}
