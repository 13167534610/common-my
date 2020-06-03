package com.common.funciton.aqmp;

import com.common.funciton.aqmp.config.QueueConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/29 17:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpTest {
    Logger logger = LoggerFactory.getLogger(AmqpTest.class);

    @Autowired
    private RabbitTemplateProvider rabbitTemplateProvider;

    @Resource(name = "myRabbitTemplate")
    private RabbitTemplate myRabbitTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * WORK模式测试
     * @throws InterruptedException
     */
    @Test
    public void testWork() throws InterruptedException {
        System.out.println("=====================================测试Work==========================================");
        HashMap<String, String> map = new HashMap<>();
        String msg = "RabbitMQ: work模式测试";
        for (int i = 0; i < 5; i++) {
            Thread.sleep(2000);
            logger.info(msg + i);
            map.put("msg", msg + i);
            rabbitTemplateProvider.sendMsg(myRabbitTemplate,"", QueueConfig.QUEUE_WORK, map);
        }

    }

    /**
     * 根据routKey由交换器发送到指定队列
     */
    @Test
    public void testDirect(){
        System.out.println("=====================================测试Direct==========================================");
        String msg = "RabbitMQ: DIRECT交换器 队列测试";
        HashMap<String, String> map = new HashMap<>();
        map.put("msg", msg);
        rabbitTemplateProvider.sendMsg(myRabbitTemplate,QueueConfig.EXCHANGE_DIRECT, QueueConfig.QUEUE_1, map);
    }

    @Test
    public void testFanout(){
        System.out.println("=====================================测试Fanout==========================================");
        String msg = "RabbitMQ: FANOUT交换器 队列测试";
        HashMap<String, String> map = new HashMap<>();
        map.put("msg", msg);
        rabbitTemplateProvider.sendMsg(myRabbitTemplate,QueueConfig.EXCHANGE_FANOUT, "", map);
    }
}
