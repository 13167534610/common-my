package com.common.funciton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
    private static HashMap<Integer, String> weekTrans;
    private static final Calendar cal = Calendar.getInstance();
    static {
        weekTrans = new HashMap<>();
        weekTrans.put(0, "日");
        weekTrans.put(1, "一");
        weekTrans.put(2, "二");
        weekTrans.put(3, "三");
        weekTrans.put(4, "四");
        weekTrans.put(5, "五");
        weekTrans.put(6, "六");
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
        return DateUtil.getSDF(formate).format(timeStamp2Date(timeStamp));
    }

    /**
     * 根据给定时间字符串获取毫秒数
     * @param dateStr
     * @param formate
     * @return
     * @throws ParseException
     */
    public static long dateStr2TimeStamp(String dateStr, String formate) throws ParseException {
        return DateUtil.getSDF(formate).parse(dateStr).getTime();
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

    /**
     * 判断date1 是否大于等于date2
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isBE(Date date1, Date date2){
        int i = date1.compareTo(date2);
        if (i >= 0) return true;
        else return false;
    }

    /**
     * 根据给定日期获取星期信息
     * @param date
     * @return
     */
    public static String getWeekInfo(Date date){
        StringBuffer result = new StringBuffer("星期");
        cal.setTime(date);
        int w=cal.get(Calendar.DAY_OF_WEEK)-1;
        result.append(weekTrans.get(w));
        return result.toString();
    }

    /**
     * 某月的第几天
     * @param date
     * @return
     */
    public static String getMonthSeq(Date date){
        SimpleDateFormat mm = DateUtil.getSDF("MM");
        String month = mm.format(date);
        StringBuffer result = new StringBuffer(month).append("月的第");
        cal.setTime(date);
        int w=cal.get(Calendar.DAY_OF_MONTH);
        result.append(w).append("天");
        return result.toString();
    }

    /**
     * 某年的第几天
     * @param date
     * @return
     */
    public static String getYearSeq(Date date){
        SimpleDateFormat mm = DateUtil.getSDF("yyyy");
        String month = mm.format(date);
        StringBuffer result = new StringBuffer(month).append("年的第");
        cal.setTime(date);
        int w=cal.get(Calendar.DAY_OF_YEAR);
        result.append(w).append("天");
        return result.toString();
    }

    /**
     * 查看给定时间在给定的单位中是第几
     * 某周中的第几天 Calendar.DAY_OF_WEEK
     * 某月中的第几天 Calendar.DAY_OF_MONTH
     * 某年中的第几天 Calendar.DAY_OF_YEAR
     * 某月中的第几周 Calendar.DAY_OF_WEEK_IN_MONTH
     * @param date
     * @param unit 单位
     * @return
     */
    public static int getDaySeq(Date date, int unit){
        cal.setTime(date);
        return cal.get(unit);
    }

    /**
     * 根据给定时间获取延迟时间
     *
     * @param date
     * @param unit 延迟单位 使用Calendar获取
     * @param n 延迟量
     * @return
     */
    public static Date getDelayTime(Date date, int unit, int n){
        cal.setTime(date);
        cal.add(unit, n);
        return cal.getTime();
    }

    /**
     * test method
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = getSDF(FORMATE_1);
        String str1 = "20191103";
        String str2 = "20191104";
        Date parse = sdf.parse(str1);
        Date parse1 = sdf.parse(str2);
        System.out.println(isBigger(parse1, parse));
    }


}
