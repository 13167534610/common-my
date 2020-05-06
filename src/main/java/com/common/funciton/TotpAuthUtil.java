package com.common.funciton;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Description: 动态密码校验
 * @Author: wangqiang
 * @Date:2020/1/17 11:04
 */
public class TotpAuthUtil {
    // 生成的key长度( Generate secret key length)
    public static final int SECRET_SIZE = 10;

    public static final String SEED = "g8GjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
    // Java实现随机数算法
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    // 最多可偏移的时间
    static int window_size = 3; // default 3 - max 17

    /**
     * set the windows size. This is an integer value representing the number of
     * 30 second windows we allow The bigger the window, the more tolerant of
     * clock skew we are.
     *
     * @param s
     *   window size - must be >=1 and <=17. Other values are ignored 5
     *   动态密码3分钟 4 动态密码2.5分钟 3 动态密码2分钟 2 动态密码1.5分钟 1 动态密码1分钟
     */
    public static void setWindowSize(int s) {
        if (s >= 1 && s <= 17)
            window_size = s;
    }

    /**
     * 生成一个随机秘钥，用于绑定用户账号
     *
     * @return secret key
     */
    public static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
     * @param user
     * @param host
     * @param secret
     * @return
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
        return String.format(format, user, host, secret);
    }

    /**
     * 生成一个OTP身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
     *
     * @param user
     *   账号
     * @param secret
     *   密钥
     * @return
     */
    public static String getQRBarcode(String user, String secret) {
        String format = "otpauth://totp/%s?secret=%s";
        return String.format(format, user, secret);
    }

    /**
     * 获取口令
     * @param secret
     * @param time
     * @return
     */
    public static long getCode(String secret, long time){
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (time / 1000L) / 30L;
        long code = 0;
        try {
            code = verify_code(decodedKey, t);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            return code;
        }
    }

    /**
     * 验证口令是否正确
     * @param secret
     * @param code
     * @param timeMsec
     * @return
     */
    public static boolean check_code(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i) { //时间偏移量
            long hash;
            try {
                hash = verify_code(decodedKey, t + i);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据二进制秘钥和时间获取口令
     * @param key
     * @param t
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }

    public static void main(String[] args) throws InterruptedException {
        //String secret = TotpAuthUtil.generateSecretKey();
        String secret = "MHN2UZ5ZGWSLATUY";
        System.out.println(secret);
        String qrcode = TotpAuthUtil.getQRBarcode("account1", secret);
        System.out.println(qrcode);

        //测试密码是否正确
        //System.out.println(check_code(secret, 420210, System.currentTimeMillis()));

        for (int i = 0; i < 10; i++) {
            long code = getCode(secret, System.currentTimeMillis());
            System.out.println(code);
            Thread.sleep(10000);
        }
    }
}
