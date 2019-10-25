package com.common.funciton;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 消息队列工具
 * @Author: wangqiang
 * @Date:2019/9/24 17:19
 */
public class ActiveMqUtil {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("hello this is test");
        list.add("first");
        list.add("two");
        list.add("three");
        //produceMessage("tcp://127.0.0.1:61616", false, Session.AUTO_ACKNOWLEDGE,list, "testQueue");


        consumeMessage("tcp://127.0.0.1:61616", false, Session.AUTO_ACKNOWLEDGE,list, "testQueue");
    }

    /**
     *
     * @param url mq访问地址
     * @return
     * @throws JMSException
     */
    private static Connection getConn(String url){
        Connection conn = null;
        try {
            ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory(url);
            conn = mqConnectionFactory.createConnection();
            conn.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }


    public static<T> void produceMessage(String url, boolean isTranSurport, int mode, List<T> messages, String queueName){
        try {
            Connection conn = getConn(url);
            if (conn != null){
                Session session = conn.createSession(isTranSurport, mode);
                Queue queue = session.createQueue(queueName);
                MessageProducer producer = session.createProducer(queue);
                if (messages.get(0) instanceof String){
                    for (T message : messages) {
                        TextMessage textMessage = session.createTextMessage(message.toString());
                        producer.send(textMessage);
                    }
                }else {

                }
                producer.close();
                session.close();
            }
            conn.close();
            System.out.println("批量发送消息成功，共" + messages.size() + "条");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static<T> T consumeMessage(String url, boolean isTranSurport, int mode, List<T> messages, String queueName){
        try {
            Connection conn = getConn(url);
            if (conn != null){
                Session session = conn.createSession(isTranSurport, mode);
                Queue queue = session.createQueue(queueName);
                MessageConsumer consumer = session.createConsumer(queue);
                consumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println("获取消息：" + textMessage.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.in.read();
                consumer.close();
                session.close();
            }
            conn.close();
        }catch (Exception e){

        }
        return null;
    }
}
