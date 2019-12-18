package com.common.funciton;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description: 数字操作工具
 * 初始化数字格式工具对象  initDecimalFormat
 * 初始化大数运算对象     initBigDecimal
 * 精准除法运算          divide
 * 数字格式化           format
 * @Author: wangqiang
 * @Date:2019/10/24 9:32
 * @创建时jdk版本 1.8
 */
public class NumberUtil {
    //百分比格式，保留3位小数
    public final static String DF_PERCENTAGE = "#.###%";
    //取整 四舍五入
    public final static String DF_ROUDING = "#";
    //取两位小数 四舍五入
    public final static String DF_POINT_2 = "#.##";
    //数字取整并分割 每三位一组
    public final static String DF_DIVISION_3 = ",###";

    //以下舍入规则 在保留小数点位数内能够除净的情况下取精确值
    //非0进1舍弃（对要求保留小数点位数的后一位若为0舍弃不为0前一位加1）
    public final static int ROUND_UP =           0;
    //结尾舍弃（对要求保留小数点位数之后的数字舍弃）
    public final static int ROUND_DOWN =         1;
    //向正无限大方向舍入 （正数按照非0进1 负数按照结尾舍弃）
    public final static int ROUND_CEILING =      2;
    //向负无限大方向舍入 （正数按照结尾舍弃 负数按照非0进1）
    public final static int ROUND_FLOOR =        3;
    //四舍五入（对要求保留小数点位数的后一位四舍五入）
    public final static int ROUND_HALF_UP =      4;
    //五舍六入（对要求保留小数点位数的后一位五舍六入）
    public final static int ROUND_HALF_DOWN =    5;
    //四舍六入五邻偶（对要求保留小数点位数的后一位四舍六入）
    public final static int ROUND_HALF_EVEN =    6;
    //断言（不允许发生舍入操作，否则抛出异常ArithmeticException）
    public final static int ROUND_UNNECESSARY =  7;

    public static void main(String[] args) {
        //System.out.println(divide((short) 3.89, (short) 2,1, ROUND_HALF_UP));
        /*System.out.println(initDecimalFormat(DF_PERCENTAGE).format(0.523965));

        System.out.println(format("3.16", 1));*/

        BigDecimal bigDecimal = new BigDecimal("15.264");
        double v = bigDecimal.setScale(2, ROUND_HALF_UP).doubleValue();
        System.out.println(v);
    }

    /**
     * 初始化数字格式化工具
     * @param format
     * @return
     *
     * @see #DF_PERCENTAGE
     * @see #DF_ROUDING
     * @see #DF_POINT_2
     * @see #DF_DIVISION_3
     */
    public static DecimalFormat initDecimalFormat(String format){
        return new DecimalFormat(format);
    }

    public static BigDecimal initBigDecimal(String v){
        return new BigDecimal(v);
    }
    public static BigDecimal initBigDecimal(short v){
        return new BigDecimal(String.valueOf(v));
    }

    public static BigDecimal initBigDecimal(int v){
        return new BigDecimal(String.valueOf(v));
    }

    public static BigDecimal initBigDecimal(float v){
        return new BigDecimal(String.valueOf(v));
    }

    public static BigDecimal initBigDecimal(double v){
        return new BigDecimal(String.valueOf(v));
    }

    /**
     * 除法
     * @param dividend   被除数
     * @param divisor    除数
     * @param point     小数点位数
     * @param carryRule 舍入规则（四舍五入 五舍六入..）
     * @return
     *
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int point, int carryRule){
        return dividend.divide(divisor, point, carryRule);
    }

    /**
     * 除法
     * @param dividend   被除数
     * @param divisor    除数
     * @param point     小数点位数
     * @param carryRule 舍入规则（四舍五入 五舍六入..）
     * @return
     *
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public static BigDecimal divide(String dividend, String divisor, int point, int carryRule){
        return NumberUtil.initBigDecimal(dividend).divide(NumberUtil.initBigDecimal(divisor), point, carryRule);
    }

    /**
     * 除法
     * @param dividend  被除数
     * @param divisor   除数
     * @param point     小数点位数
     * @param carryRule 舍入规则（四舍五入 五舍六入..）
     * @return
     *
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public static BigDecimal divide(double dividend, double divisor, int point, int carryRule){
        return NumberUtil.initBigDecimal(dividend).divide(NumberUtil.initBigDecimal(divisor), point, carryRule);
    }

    /**
     * 除法
     * @param dividend  被除数
     * @param divisor   除数
     * @param point     小数点位数
     * @param carryRule 舍入规则（四舍五入 五舍六入..）
     * @return
     *
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public static BigDecimal divide(float dividend, float divisor, int point, int carryRule){
        return NumberUtil.initBigDecimal(dividend).divide(NumberUtil.initBigDecimal(divisor), point, carryRule);
    }

    /**
     * 除法
     * @param dividend  被除数
     * @param divisor   除数
     * @param point     小数点位数
     * @param carryRule 舍入规则（四舍五入 五舍六入..）
     * @return
     *
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public static BigDecimal divide(int dividend, int divisor, int point, int carryRule){
        return NumberUtil.initBigDecimal(dividend).divide(NumberUtil.initBigDecimal(divisor), point, carryRule);
    }

    /**
     * 除法
     * @param dividend  被除数
     * @param divisor   除数
     * @param point     小数点位数
     * @param carryRule 舍入规则（四舍五入 五舍六入..）
     * @return
     *
     * @see #ROUND_UP
     * @see #ROUND_DOWN
     * @see #ROUND_CEILING
     * @see #ROUND_FLOOR
     * @see #ROUND_HALF_UP
     * @see #ROUND_HALF_DOWN
     * @see #ROUND_HALF_EVEN
     * @see #ROUND_UNNECESSARY
     */
    public static BigDecimal divide(short dividend, short divisor, int point, int carryRule){
        return NumberUtil.initBigDecimal(dividend).divide(NumberUtil.initBigDecimal(divisor), point, carryRule);
    }

    /**
     * 数字格式化
     * @param number 数字
     * @param format 格式
     * @return
     *
     * @see #DF_PERCENTAGE
     * @see #DF_ROUDING
     * @see #DF_POINT_2
     * @see #DF_DIVISION_3
     */
    public static String format (String number, String format){
        return NumberUtil.initDecimalFormat(format).format(NumberUtil.initBigDecimal(number).doubleValue());
    }

    /**
     * 数字格式化
     * @param number 数字
     * @param df 格式化工具
     * @return
     */
    public static String format (String number, DecimalFormat df){
        return df.format(NumberUtil.initBigDecimal(number).doubleValue());
    }

    /**
     * 数字格式化
     * 截去小数点保留位数之后的数字
     * @param number 数字
     * @param point 保留小数点后几位
     * @return
     */
    public static String format(String number, int point){
        if (!number.contains("\\."))number = String.valueOf(initBigDecimal(number).doubleValue());
        String[] split = number.split("\\.");
        StringBuffer sb = new StringBuffer(split[0]);
        sb.append(".").append(split[1].substring(0, point));
        return sb.toString();
    }
}
