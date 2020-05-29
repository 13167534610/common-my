package com.common.algorithm;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/1/9 14:22
 */
public class Banana extends Fruit{
    public String name = "香蕉";

    public void printName(){
        System.out.println(name);
    }

    @Override
    public String toString() {
        return "Banana{" +
                "name='" + name + '\'' +
                '}';
    }
}
