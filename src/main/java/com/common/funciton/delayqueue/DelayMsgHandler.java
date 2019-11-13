package com.common.funciton.delayqueue;

import com.common.funciton.DateUtil;
import com.common.funciton.Identification;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * @Description: 延迟队列使用案例
 * @Author: wangqiang
 * @Date:2019/11/8 18:56
 */

public class DelayMsgHandler {

    private static DelayQueue<DelayMsg> delayQueue =  new DelayQueue<DelayMsg>();

    public void init(){
        System.out.println("自动方法执行:" + delayQueue.size());
        DelayMsg massage = null;
        while (true){
            try {
                massage = delayQueue.take();//没有消息会阻塞
                long expire = massage.getExpire();
                if (DateUtil.isBE(new Date(), new Date(expire))){
                    System.out.println(DateUtil.getStrSysDate(DateUtil.FORMATE_4) + "任务处理" + massage.toString() + ", 当前队列长度" + delayQueue.size());
                }else {
                    System.out.println(DateUtil.getStrSysDate(DateUtil.FORMATE_4) + "没到时间, 当前队列长度" + delayQueue.size());
                    delayQueue.add(massage);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean addTask(DelayMsg delayMsg){
        boolean add = delayQueue.add(delayMsg);
        return add;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(DateUtil.getStrSysDate(DateUtil.FORMATE_4));
        for (int i = 1; i < 5; i++) {
            int n = i + 10;
            DelayMsg delayMsg = new DelayMsg(Identification.uuid32(), new Date().getTime(), n, "这是第" + i + "个任务，延迟" + n + "s执行");
            System.out.println(delayMsg.toString());
            delayQueue.add(delayMsg);
            //Thread.sleep(2000);
        }
        System.out.println("任务添加完成" + DateUtil.getStrSysDate(DateUtil.FORMATE_4));

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("DelayTest.xml");
        /*DelayMsgHandler delayMsgHandler = (DelayMsgHandler) context.getBean("delayMsgHandler");
        delayMsgHandler.init();*/
        Thread.sleep(1000000);
    }
}
