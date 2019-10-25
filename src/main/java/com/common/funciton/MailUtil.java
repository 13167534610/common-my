package com.common.funciton;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @Description: 邮件工具类
 * 群发带有附件的邮件 sendAttachmentMail2Group
 * 群发文本文件 sendTextMail2Group
 * 单发文本文件 sendTextMail2Single
 * 向单个邮箱发送带有附件的邮件 sendAttachmentMail2Single
 * @Author: wangqiang
 * @Date:2019/9/3 15:26
 * @创建时jdk版本 1.8
 */
public class MailUtil {
    //本地测试
    public static final String smtpHost = "10.6.4.78";
    //发件人地址
    public static final String from = "system_monitor@zihexin.com";
    //邮箱账户
    public static final String name = "system_monitor@zihexin.com";
    //邮箱密码
    public static final String password = "000000";

    private static JavaMailSenderImpl mailSender;
    static {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setUsername(from);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.timeout", "2000");
        mailSender.setJavaMailProperties(properties);
    }
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("wangqiang@zihexin.com");
        list.add("13167534610@163.com");
        HashMap<String, InputStreamSource> map = new HashMap<>();
        ByteArrayResource resource1 = new ByteArrayResource("test1\nthis is test1".getBytes());
        ByteArrayResource resource2 = new ByteArrayResource("test2\nthis is test1".getBytes());
        map.put("test1.txt", resource1);
        map.put("test2.txt", resource2);
        sendAttachmentMail2Group(map,"您好认知","很高心认识您", list);
    }
    /**
     * 向组内发送带有N个附件的邮件
     * @param issMap
     * @param subject
     * @param msg
     * @param toList
     */
    public static void sendAttachmentMail2Group(HashMap<String, InputStreamSource> issMap, String subject, String msg, List<String> toList){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = packMailContent(issMap, subject, msg, message);
            groupSend(toList, helper, message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 群发文本邮件
     * @param subject
     * @param msg
     * @param toList
     */
    public static void sendTextMail2Group(String subject, String msg, List<String> toList){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = packMailContent(null, subject, msg, message);
            groupSend(toList, helper, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 向单个邮箱发送文本邮件
     * @param subject
     * @param msg
     * @param toMail
     */
    public static void sendTextMail2Single(String subject, String msg, String toMail){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = packMailContent(null, subject, msg, message);
            helper.setTo(toMail);
            mailSender.send(message);//发送
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 向单个邮箱发送带有附件的邮件
     * @param issMap
     * @param subject
     * @param msg
     * @param toMail
     */
    public static void sendAttachmentMail2Single(HashMap<String, InputStreamSource> issMap, String subject, String msg, String toMail){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = packMailContent(issMap, subject, msg, message);
            helper.setTo(toMail);
            mailSender.send(message);//发送
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 邮件内容组装
     * @param issMap
     * @param subject
     * @param msg
     * @param message
     * @return
     */
    private static MimeMessageHelper packMailContent(HashMap<String, InputStreamSource> issMap, String subject, String msg, MimeMessage message){
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(name);
            helper.setSubject(subject);//设置主题
            helper.setText(msg, true);//设置邮件内容
            if (issMap != null && !issMap.isEmpty()){//设置附件
                for (String fileName : issMap.keySet()) {
                    InputStreamSource source = issMap.get(fileName);
                    helper.addAttachment(fileName, source);
                }
            }
            return helper;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 组内发送动作
     * @param toList
     * @param helper
     * @param message
     */
    private static void groupSend(List<String> toList, MimeMessageHelper helper, MimeMessage message){
        for (String s : toList) {
            try {
                helper.setTo(s);
                mailSender.send(message);//发送
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("发送到邮箱"+ s + "失败");
            }
        }
    }



}
