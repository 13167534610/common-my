package com.common.funciton;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/8/20 17:15
 */
public class ShellUtil {
    public static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    public static String showJavaProcess =  "ps -aux|grep java";


    /**
     * 登陆Linux
     * @param param host地址, port端口，userName账户， password密码
     * @return
     */
    public static boolean login(ShellParam param) {
        try {
            Connection conn = threadLocal.get();
            if (conn == null){
                if (param.getPort() == null)conn = new Connection(param.getHost());
                else conn = new Connection(param.getHost(), param.getPort());
                threadLocal.set(conn);
                param.setConn(conn);
            }
            conn.connect();
            boolean b = conn.authenticateWithPassword(param.getUserName(), param.getPassword());
            return b;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String remoteExec(String cmds) throws IOException {
        Process pro = Runtime.getRuntime().exec(cmds);
        InputStream inputStream = pro.getInputStream();
        String s = processStdout(inputStream, "utf-8");
        return s;
    }

    /**
     * 执行命令返回结果
     * @param param
     * @return
     */
    public static String loginExec(ShellParam param){
        InputStream in = null;
        String result = "";
        try {
            if (login(param)) {
                Connection conn = param.getConn();
                Session session = conn.openSession(); // 打开一个会话
                session.execCommand(param.getCmds());
                in = session.getStdout();
                result = processStdout(in, "utf-8");
                session.close();
                conn.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    /**
     * 解析返回结果流
     * @param in
     * @param charset
     * @return
     */
    public static String processStdout(InputStream in, String charset) {

        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            while (in.read(buf) != -1) {
                sb.append(new String(buf, charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static class ShellParam{
        private String host;

        private Integer port;

        private String cmds;

        private String userName;

        private String password;

        private Connection conn;

        public String getCmds() {
            return cmds;
        }

        public void setCmds(String cmds) {
            this.cmds = cmds;
        }

        public Connection getConn() {
            return conn;
        }

        public void setConn(Connection conn) {
            this.conn = conn;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
