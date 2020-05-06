package com.common.funciton;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具
 */
public class ThreeDES {

    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish

    private final static byte[] keyBytes = {0x61, 0x42, (byte) 0x8F, 0x38, (byte)0x88, 0x17, 0x40, 0x38
            , 0x28, 0x35, 0x79, 0x51, (byte)0xCB, (byte)0xDD, 0x55, 0x66
            , 0x76, 0x29, (byte) 0x84, (byte)0x98, 0x40, 0x40, 0x46, (byte)0xE8};    //24字节的密钥
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）

    /**
     * 加密
     * @param src 加密字符
     * @return
     */
    public static byte[] encryptMode(byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区

    /**
     * 解密
     * @param src  解密字符
     * @return
     */
    public static byte[] decryptMode(byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }

    public static void main(String[] args)
    {
        //添加新安全算法,如果用JCE就要把它添加进去
//        Security.addProvider(new com.sun.crypto.provider.SunJCE());
//
//
//        String szSrc = "xq_7830000000001073921";
//        System.out.println("加密前的字符串:" + szSrc);
//
//        byte[] encoded = encryptMode(szSrc.getBytes());
//        System.out.println("加密后的字符串:" + byte2Base64(encoded));
//
        String aa="30c30xb9BZXL4tVmskacnCyY9dQlE5dk";
        byte[] bb= Base64.decode(aa);

        byte[] srcBytes = decryptMode(bb);
        System.out.println("解密后的字符串:" + new String(srcBytes));
    }

    /**
     * 转换成base64编码
     *
     * @param b
     * @return
     */
    public static String byte2Base64(byte[] b) {
        return Base64.encode(b);

    }

}
