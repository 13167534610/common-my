package com.common.funciton.aqmp;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/6/2 9:00
 */
@Component
public class RabbitAdminProvider {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    public void sendMsg(){

    }
}
