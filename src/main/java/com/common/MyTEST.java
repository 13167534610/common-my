package com.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/26 16:11
 */
public class MyTEST {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String path = "E:\\personal files manage\\IMS\\";
        String encode = URLEncoder.encode(path, "UTF-8");
        System.out.println(encode);
    }
}
