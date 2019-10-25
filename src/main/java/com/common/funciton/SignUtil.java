package com.common.funciton;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Description: 本类为签名类，均为静态方法，包含MD5 BASE64 ....
 * @Author: wangqiang
 * @Date:2018/12/29 13:41
 */
public class SignUtil {
    public static void main(String[] args) {
        String s = AESEncode("WQ", "你好");
        System.out.println(s);
        String s1 = AESDncode("WQ", "LtA/f0IPNH7bowe9EzUh3g==");
        System.out.println(s1);
    }
    /**
     * MD5签名，签名结果不可逆转
     * @param s
     * @return
     */
    public static String MD5encrypt(String s) {
        if (s.isEmpty())return null;
        // 用作十六进制的数组
        byte hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");// 使用MD5加密
            byte[] strTemp = s.getBytes("utf-8");// 把传入的字符串转换成字节数组
            mdTemp.update(strTemp);//
            byte[] md = mdTemp.digest();
            int j = md.length;
            byte str[] = new byte[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);// 返回加密后的字符串.
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 位运算加解密，号码检查，将铭文字符串传入可生成密文，将密文传入可还原成铭文
     * @param s
     * @return
     */
    public static String checkCode(String s){
        byte[] b0 = {1, 3, 5};
        String result = null;
        if (StringUtils.isNotBlank(s)){
            byte[] b = s.getBytes();
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) (b[i] ^ b0[0]);
            }
            result = new String(b);
        }
        return result;
    }

    /**
     * AES对称加密
     * @param encodeRules 密钥
     * @param content 原文
     * @return 密文
     */
    public static String AESEncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen= KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte [] byte_encode=content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte [] byte_AES=cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode=new String(new BASE64Encoder().encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (Exception e) {
            e.printStackTrace();
            //如果有错就返加nulll
            return null;
        }
    }

    /**
     * AES对称解密
     * @param encodeRules 密钥
     * @param content 密文
     * @return 原文
     */
    public static String AESDncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen= KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    /**
     * 编码
     * @param data
     * @return
     */
    public static String base64Encode(String data){
        if (StringUtils.isNotBlank(data)){
            return Base64.encodeBase64String(data.getBytes());
        }
        return "";
    }

    /**
     * 解码
     * @param data
     * @return
     * @throws IOException
     */
    public static String base64decode(String data){
        if (StringUtils.isNotBlank(data)){
            byte[] bytes = Base64.decodeBase64(data.getBytes());
            return new String(bytes);
        }
        return "";
    }

    /**
     * 将参数安属性名排序转换字符串
     * @param map
     * @return
     */
    public static String sortParamsToStr(Map<String,Object> map){
        if (null == map || map.isEmpty())return null;
        Set<String> strings = map.keySet();
        ArrayList<String> list = new ArrayList<>();
        for (String string : strings) {
            list.add(string);
        }
        StringBuffer buffer = new StringBuffer("");
        Collections.sort(list);
        for (String s : list) {
            Object o = map.get(s);
            if (null != o)
                buffer.append(s).append(o.toString());
        }
        return buffer.toString();
    }

}
