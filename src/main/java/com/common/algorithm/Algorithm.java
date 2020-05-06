package com.common.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/12/27 14:46
 */
public class Algorithm {

    public static void main(String[] args) {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(3L);
        longs.add(2L);
        longs.add(3L);
        longs.add(7L);
        longs.add(5L);
        longs.add(8L);
        System.out.println(getMaxVlue1(longs));
        System.out.println(getMaxValue2(longs));
        System.out.println(getMaxValue3(longs));
    }

    /**
     * 逐级查找
     * @param numbers
     * @return
     */
    public static Long getMaxVlue1(List<Long> numbers){
        Long temp = null;
        Long result = numbers.get(0);
        int count = 0;
        for (int i = 0; i < numbers.size() - 1; i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                temp =  numbers.get(i) > numbers.get(j) ? numbers.get(i) : numbers.get(j);
                result = result > temp ? result : temp;
                count++;
            }
        }
        System.out.println("比较次数" + count);
        return result;
    }

    /**
     * 折半查找
     * @param numbers
     * @return
     */
    public static Long getMaxValue2(List<Long> numbers){
        List<Long> longs = numbers.subList(0, numbers.size() / 2);
        List<Long> longs1 = numbers.subList(numbers.size() / 2, numbers.size());
        Long maxVlue1 = getMaxVlue1(longs);
        Long maxVlue11 = getMaxVlue1(longs1);
        return maxVlue1 > maxVlue11 ? maxVlue1 : maxVlue11;
    }

    /**
     * 冒泡查找
     * @param numbers
     * @return
     */
    public static Long getMaxValue3(List<Long> numbers){
        int count = 0;
        Long result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = result > numbers.get(i) ? result : numbers.get(i);
             count++;
        }
        System.out.println("比较次数" + count);
        return result;
    }
}
