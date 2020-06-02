package com.common.funciton.aqmp.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 16:42
 */
@Component
public class QueueConfig {

    public static final String QUEUE_1 = "queue1";
    public static final String QUEUE_2 = "queue2";

    public static final String EXCHANGE_DIRECT = "directExchange";
    public static final String EXCHANGE_FANOUT = "fanoutExchange";
    public static final String EXCHANGE_TOPIC = "topicExchange";
    public static final String EXCHANGE_HEADERS = "headersExchange";

    @Bean(name = "queue1")
    public Queue queue1(){
        /**
         * 测试队列：
         * name: 队列名称
         * durable: 持久化
         * exclusive：是否排外（只允许当前连接访问，当前连接断开后排外队列自动清除）
         * autoDelete：是否自动清除（在最后一个连接断开的时候, 服务停止）
         * arguments：设置队列参数达到不同效果，例如延迟队列
         */
        return new Queue("queue1", true, false, false, null);
    }

    @Bean(name = "queue2")
    public Queue queue2(){
        /**
         * 测试队列：
         * name: 队列名称
         * durable: 持久化
         * exclusive：是否排外（只允许当前连接访问，当前连接断开后排外队列自动清除）
         * autoDelete：是否自动清除（在最后一个连接断开的时候, 服务停止）
         * arguments：设置队列参数达到不同效果，例如延迟队列
         */
        Queue simpleQueue = new Queue("queue2", true, false, false, null);
        return simpleQueue;
    }

    @Bean(name = "directExchange")
    public DirectExchange directExchange(){
        /**
         * 测试交换器：
         * name: 队列名称
         * durable: 持久化
         * autoDelete：是否自动清除（在最后一个连接断开的时候, 服务停止）
         * arguments：设置队列参数达到不同效果，例如延迟队列
         */
        return new DirectExchange("directExchange", true, false, null);
    }

    @Bean(name = "fanoutExchange")
    public FanoutExchange fanoutExchange(){
        /**
         * 测试交换器：
         * name: 队列名称
         * durable: 持久化
         * autoDelete：是否自动清除（在最后一个连接断开的时候, 服务停止）
         * arguments：设置队列参数达到不同效果，例如延迟队列
         */
        return new FanoutExchange("fanoutExchange", true, false, null);
    }

    @Bean(name = "topicExchange")
    public TopicExchange topicExchange(){
        /**
         * 测试交换器：
         * name: 队列名称
         * durable: 持久化
         * autoDelete：是否自动清除（在最后一个连接断开的时候, 服务停止）
         * arguments：设置队列参数达到不同效果，例如延迟队列
         */
        return new TopicExchange("topicExchange", true, false, null);
    }

    @Bean(name = "headersExchange")
    public HeadersExchange headersExchange(){
        /**
         * 测试交换器：
         * name: 队列名称
         * durable: 持久化
         * autoDelete：是否自动清除（在最后一个连接断开的时候, 服务停止）
         * arguments：设置队列参数达到不同效果，例如延迟队列
         */
        return new HeadersExchange("headersExchange", true, false, null);
    }

    /**
     * 绑定directExchange和队列queue1，路由key：queue1
     * @param queue1
     * @param directExchange
     * @return
     */
    @Bean(name = "directBindQueue1")
    public Binding directBindQueue1(@Qualifier("queue1") Queue queue1, @Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue1).to(directExchange).with(queue1.getName());
    }

    /**
     * 绑定directExchange和队列 queue2，路由key：queue2
     * @param queue2
     * @param directExchange
     * @return
     */
    @Bean(name = "directBindQueue2")
    public Binding directBindQueue2(@Qualifier("queue2") Queue queue2, @Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue2).to(directExchange).with(queue2.getName());
    }


    @Bean(name = "fanoutBindQueue1")
    public Binding fanoutBindQueue1(@Qualifier("queue1") Queue queue1, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }
    @Bean(name = "fanoutBindQueue2")
    public Binding fanoutBindQueue2(@Qualifier("queue2") Queue queue2, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }
}
