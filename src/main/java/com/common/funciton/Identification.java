package com.common.funciton;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

/**
 * @Description: id生产工具
 * 获取36位的uuid uuid36(带有“-”)
 * 获取32位的uuid uuid32(没有“-”)
 * 根据时间获取自定义编号 dateNumberId
 * 根据luhn算法获取自定义编号 luhnNumberId
 * 获取指定长度的随机数字符串 getRandomNumber
 * @Author: wangqiang
 * @Date:2019/6/3 23:15
 * @创建时jdk版本 1.8
 */
public class Identification {
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("0.00%");
        HashSet<String> strings = new HashSet<>();
        //BigDecimal sum = new BigDecimal("0");
        double sum = 0.00D;
        int max = 1000000;
        for (int i = 0; i < max; i++) {
            String number = getRandomNumber(7);
            strings.add(number);
            //System.out.println(number);
        }
        System.out.println(df.format(new BigDecimal(max - strings.size()).divide(new BigDecimal(max),5, BigDecimal.ROUND_HALF_UP)));
        /*System.out.println(sum.add(new BigDecimal("1")).doubleValue());
        System.out.println(sum.add(new BigDecimal("1")).doubleValue());*/

    }

    /**
     * 返回36位带有“-”的字符串
     * @return
     */
    public static String uuid36(){
        return UUID.randomUUID().toString();
    }

    /**
     * 返回去除“-”的字符串  32位
     * @return
     */
    public static String uuid32(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取自定义20位唯一标识  yyyyMMddHHmmss + seq + 随机位
     * 适合用作订单号 流水号 针对高并发可提高随机位长度如length = 32 seq=null
     * seq 若为空返回长度为20
     * @return
     */
    public static String dateNumberId(int length, String seq){
        StringBuffer iDBuffer = null;
        if (StringUtils.isBlank(seq))seq = "";
        int i = seq.length() + 20;//保留6位随机位 卡号最短需要20位
        if (i >=length) return null;
        Date sysDate = new Date();
        String dateStr = DateUtil.dateToStr(sysDate, "yyyyMMddHHmmss");
        iDBuffer = new StringBuffer(dateStr);
        iDBuffer.append(seq);
        iDBuffer.append(getRandomNumber(length - i));
        return iDBuffer.toString();
    }

    /**
     * 根据luhn算法获取纯数字编号
     * 编号长度必须大于bin号长度与流水号长度之和，否则返回null
     * 适用于 卡券号
     * @param length 需要编号长度
     * @param bin bin号
     * @param seq 流水号
     * @return
     */
    public static String luhnNumberId(int length, String bin, String seq){
        if (StringUtils.isBlank(bin))bin = "";
        if (StringUtils.isBlank(seq))seq = "";
        int binLen = bin.length();
        int seqLen = seq.length();
        if ((binLen + seqLen) >= length)return null;
        int randomLen = length - binLen - seqLen - 1;
        String randomNumber = getRandomNumber(randomLen);//获取指定位数的随机数
        StringBuffer cardNoBuffer = new StringBuffer(bin).append(randomNumber).append(seq);
        String validNum = getValidNum(cardNoBuffer.toString());
        cardNoBuffer.append(validNum);
        return cardNoBuffer.toString();
    }

    /**
     * 获取指定位数的随机数字字符串
     * 6位测试100W次 重复率大概为36%
     * 7位测试100W次 重复率大概为5%
     * 8位测试100W次 重复率大概为0.5%
     * @param size 随机数字符串长度
     * @return
     */
    public static String getRandomNumber(int size){
        if (size < 1) return "";
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < size; i++) {
            sb.append(String.valueOf(Math.random() * 10).charAt(0));
        }
        return sb.toString();
    }

    /**
     * 根据数字字符串通过luhn算法获取校验位
     * @param beforeId 除校验位之前的数字字符串 不能为空否则返回null
     * @return validNum
     * luhn算法
     * cardNo = beforeId+validNum 从右向左给字符编号i (1-n)并遍历对给个字符对应的数字（t）的操作结果相加sum
     * 操作流程如下：如果i是奇数位则直接加上该字符数字(sum+=t)
     *      如果i是偶数位则先将tmp = t*2 如果tmp<10 那么加上tmp（sum+=tmp）否则加上tmp的个位与十位的和（sum+=tmp/10 + tmp%10）
     * 如果sum能够整除10（sum%10 == 0）则cardNo为有效卡号
     */
    private static String getValidNum(String beforeId) {
        String validNum = null;
        if (StringUtils.isNotBlank(beforeId)) {
            int sum = 0;
            char[] chars = beforeId.trim().toCharArray();
            int charLen = chars.length;
            int n = 0;
            for (int i = charLen - 1; i >= 0; i--) {
                if ((charLen + 1 - i) % 2 != 0) sum += Character.digit(chars[i], 10);//从右向左数的位数如果是奇数位则直接加上当前值
                else {
                    n = Character.digit(chars[i], 10) * 2;
                    if (n > 9) n = n / 10 + n % 10;//获取十位数和个位数的和
                    sum += n;
                }
            }
            validNum = String.valueOf((sum % 10 == 0) ? 0 : (10 - sum % 10)); //获取校验位
        }
        return validNum;
    }
}
