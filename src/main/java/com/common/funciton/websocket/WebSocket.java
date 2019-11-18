package com.common.funciton.websocket;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/11/14 10:34
 */
@Component
@ServerEndpoint("/pushMsg/{topic}/{clientIp}")
public class WebSocket {
    private static ConcurrentHashMap<String, ArrayList<String>> subscribeIps = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ArrayList<Session>> subscribeSessions= new ConcurrentHashMap<>();

    @OnOpen
    public void subscribe(@PathParam("topic") String topic, @PathParam("clientIp") String clientIp, Session session){
        ArrayList<String> ips = subscribeIps.get(topic);
        ArrayList<Session> sessions = subscribeSessions.get(topic);
        if (!CollectionUtils.isEmpty(ips)){
            if (!ips.contains(clientIp)){
                ips.add(clientIp);
                sessions.add(session);
                subscribeSessions.put(topic, sessions);
                subscribeIps.put(topic, ips);
            }
        }else {
            ips = new ArrayList<>();
            sessions = new ArrayList<>();
            ips.add(clientIp);
            sessions.add(session);
            subscribeSessions.put(topic, sessions);
            subscribeIps.put(topic, ips);
        }
        System.out.println(clientIp + "已订阅" + topic);
        System.out.println(topic + "当前订阅量：" + ips.size());
    }

    @OnClose
    public void cancleSubscribe(@PathParam("topic") String topic, @PathParam("clientIp") String clientIp, Session session){
        ArrayList<String> ips = subscribeIps.get(topic);
        ArrayList<Session> sessions = subscribeSessions.get(topic);
        ips.remove(clientIp);
        sessions.remove(session);
        System.out.println();
        System.out.println(topic + "当前订阅量：" + ips.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message, String topic) throws IOException {
        ArrayList<Session> sessions = subscribeSessions.get(topic);
        if (CollectionUtils.isEmpty(sessions)){
            System.out.println("无人订阅" + topic);
        }else {
            System.out.println(topic + "订阅人数" + sessions.size() + ", 正在推送中");
            for (Session session : sessions) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
}
