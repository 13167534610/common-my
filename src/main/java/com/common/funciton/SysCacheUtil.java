package com.common.funciton;


import com.common.algorithm.Apple;
import com.common.algorithm.Banana;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/28 16:09
 */
public class SysCacheUtil {

    private static ConcurrentHashMap<String, Object> cache;

    /**
     * 单例模式创建缓存实例
     * @return
     */
    private static ConcurrentHashMap<String, Object> chcheInstance(){
        if (cache == null){
            synchronized (ConcurrentHashMap.class){
                if (cache == null){
                    cache = new ConcurrentHashMap();
                }
            }
        }
        return cache;
    }

    /**
     * 添加缓存
     * @param k
     * @param v
     * @return
     */
    public static Object setCache(String k, Object v){
        ConcurrentHashMap<String, Object> cacheMap = chcheInstance();
        Object put = cacheMap.put(k, v);
        return put;
    }

    /**
     * 获取缓存
     * @param k
     * @param <T>
     * @return
     */
    public static Object getCachValue(String k){
        ConcurrentHashMap<String, Object> chcheInstance = chcheInstance();
        return chcheInstance.get(k);
    }

    /**
     * 获取指定类型缓存
     * @param k
     * @param clazz
     * @param <T>
     * @return
     */
    public static<T extends Object> T getCachValue(String k, Class<T> clazz){
        Object cachValue = getCachValue(k);
        if (cachValue != null)return (T)cachValue;
        else return null;
    }

    /**
     * 根据key删除缓存
     * @param k
     * @return
     */
    public static Object removeCacheByK(String k){
        ConcurrentHashMap<String, Object> chcheInstance = chcheInstance();
        return chcheInstance.remove(k);
    }

    /**
     * 清空缓存
     */
    public static void clearCache(){
        ConcurrentHashMap<String, Object> chcheInstance = chcheInstance();
        chcheInstance.clear();
    }

    public static void main(String[] args) {
        Apple apple = new Apple();
        Object name = SysCacheUtil.setCache("fruit", apple.bananas);
        System.out.println(name);

        ArrayList<Banana> fruit = SysCacheUtil.getCachValue("fruit", ArrayList.class);
        System.out.println(fruit);


    }
}
