package com.common.funciton;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: date util
 *
 * 获取时间格式化工具        getSDF
 * 时间戳转时间             timeStamp2Date
 * 时间戳转时间字符串        timeStamp2DateStr
 * 时间字符串转时间戳        dateStr2TimeStamp
 * 获取当前时间字符串        getStrSysDate
 * 字符串转时间             strToDate
 * 时间转字符串             dateToStr
 * 判断时间1是否比时间2大     isBigger
 * 判断时间1是否比时间2小     isSmaller
 * 判断时间1等于时间2大      isEquals
 *
 * @Author: wangqiang
 * @Date:2019/6/3 14:47
 *
 * @创建时jdk版本 1.8
 */
public class DateUtil {
    public final static String FORMATE_1 = "yyyyMMdd";
    public final static String FORMATE_2 = "yyyy/MM/dd";
    public final static String FORMATE_3 = "yyyy_MM_dd";
    public final static String FORMATE_4 = "yyyyMMdd HH:mm:ss";
    public final static String FORMATE_5 = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMATE_6 = "yyyy_MM_dd HH:mm:ss";
    public final static String FORMATE_7 = "yyyyMMddHHmmss";

    /**
     * test method
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        System.out.println(dateToStr(new Date(), FORMATE_4));
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("");
        context.getBean("");
    }

    /**
     * you can use this method to get a object which type is SimpleDateFormat
     * @param formate
     * @return
     */
    public static SimpleDateFormat getSDF(String formate){
        return new SimpleDateFormat(formate);
    }

    /**
     * 根据毫秒数获取时间
     * @param timeStamp
     * @return
     */
    public static Date timeStamp2Date(Long timeStamp){
        return new Date(timeStamp);
    }

    /**
     * 根据毫秒数获取指定格式的时间字符串
     * @param timeStamp 毫秒数
     * @param formate 时间格式
     * @return
     */
    public static String timeStamp2DateStr(Long timeStamp, String formate){
        SimpleDateFormat sdf = getSDF(formate);
        return sdf.format(timeStamp2Date(timeStamp));
    }

    /**
     * 根据给定时间字符串获取毫秒数
     * @param dateStr
     * @param formate
     * @return
     * @throws ParseException
     */
    public static long dateStr2TimeStamp(String dateStr, String formate) throws ParseException {
        return getSDF(formate).parse(dateStr).getTime();
    }

    /**
     * you can use this method to get system'date now that type is string by custom formate
     * @param formate 日期格式
     * @return
     */
    public static String getStrSysDate(String formate){
        return DateUtil.getSDF(formate).format(new Date());
    }

    /**
     * you can use this method to get date by string dadte and formate
     * @param dateStr
     * @param formate
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String formate) throws ParseException {
        return DateUtil.getSDF(formate).parse(dateStr);
    }

    /**
     * you can use thos method to get string date by date and formate
     * @param date
     * @param formate
     * @return
     */
    public static String dateToStr(Date date, String formate){
        return DateUtil.getSDF(formate).format(date);
    }

    /**
     * you can use this method to judge date1 is bigger than date2
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isBigger(Date date1, Date date2){
        int i = date1.compareTo(date2);
        if (i > 0) return true;
        else return false;
    }

    /**
     * you can use this method to judge date1 is smaller than date2
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSmaller(Date date1, Date date2){
        int i = date1.compareTo(date2);
        if (i < 0) return true;
        else return false;
    }

    /**
     * you can use this method to judge date1 is equals than date2
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEquals(Date date1, Date date2){
        int i = date1.compareTo(date2);
        if (i == 0) return true;
        else return false;
    }


}
