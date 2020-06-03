package com.common.funciton.aqmp;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/6/2 9:00
 */
@Component
public class RabbitAdminProvider {

    @Resource(name = "myRabbitAdmin")
    private RabbitAdmin myRabbitAdmin;

    public void sendMsg(){

    }
}
