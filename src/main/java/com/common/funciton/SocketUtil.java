package com.common.funciton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/8/6 16:53
 */
public class SocketUtil {

    public static HashMap<String, String> SocketServer(int port, String encoding){
        HashMap<String, String> result = new HashMap<>();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket accept = serverSocket.accept();//线程阻塞等待请求
            InetAddress inetAddress = accept.getInetAddress();
            InputStream is = accept.getInputStream();
            String s = readMsg(is);
            is.close();
            accept.close();
            serverSocket.close();
            result.put("code","success");
            result.put("msg", s);
            result.put("remoteIp", inetAddress.getHostAddress());
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", "error");
        }
        return result;
    }

    public static boolean socketClient(int port, String host, String msg, String encoding){
        boolean flag = true;
        try {
            Socket socket = new Socket(host, port);
            OutputStream os = socket.getOutputStream();
            os.write(msg.getBytes(encoding));
            os.close();
            socket.close();
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    public static String readMsg(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = is.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        return sb.toString();
    }



}
