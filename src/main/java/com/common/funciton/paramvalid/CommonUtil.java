package com.common.funciton.paramvalid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by wangqiang on 2019/8/5.
 */
public class CommonUtil {
    /**
     * 属性空值校验  检验对象的属性值是否为空，如果为空则返回需要的错误信息
     * 此方法可能抛出三种异常 InvocationTargetException, NoSuchMethodException, IllegalAccessException
     * @param o             校验对象
     * @param clazz         标记注解类
     * @param methodName    标记注解类中的提示方法名
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static String validNullField(Object o, Class clazz, String methodName){
        if (o == null)return "未接收到参数";
        ArrayList<Field> list = new ArrayList<>();
        Class tempClass = o.getClass();
        //循环获取对象属性和父级对象知道Object
        while (tempClass != null){
            Field[] fields = tempClass.getDeclaredFields();
            list.addAll(Arrays.asList(fields));
            tempClass =  tempClass.getSuperclass();
        }
        for (Field field : list) {
            Annotation annotation = field.getAnnotation(clazz);
            if (annotation != null){
                //开启属性访问权限
                field.setAccessible(true);
                try {
                    //获取对象的提示方法
                    Method noticeMessageMethod = clazz.getDeclaredMethod(methodName);
                    //执行提示方法获取提示信息（信息默认为字符串）
                    String message = (String) noticeMessageMethod.invoke(annotation, new Object[]{});
                    //获取该属性对象
                    Object fieldV = field.get(o);
                    if (fieldV instanceof String && StringUtils.isBlank((String)fieldV)){
                        return message;
                    }else if (null == fieldV){
                        return message;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return "参数检查出现异常";
                }
            }
        }
        return null;
    }


    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        RegistParam param = new RegistParam();
        param.setPassword("12312");
        //param.setPhone("234234");
        param.setCommunicationId("ftyuhsjfgjiosdol");
        param.setSwapType("sjldfjlkds");
        param.setRequstIp("127.0.0.1");
        param.setRequstTime(new Date().getTime());
        param.setSign("slakjdflkas");
        String s = validNullField(param, ParamNotNull.class, "message");
        System.out.println(s);

        /*HashMap map = new HashMap();
        map.put("time", new Date().getTime());
        Long time = (Long)map.get("time");
        System.out.println(time);*/
    }
}
