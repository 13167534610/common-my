package com.common.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/1/9 14:12
 */
public class Apple extends Fruit{
    public String name = "苹果";

    public List<Banana> bananas = new ArrayList<>();
    public void printName(){
        System.out.println(name);
    }

    public Apple() {
        Banana banana = new Banana();
        bananas.add(banana);
    }

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                ", bananas=" + bananas +
                '}';
    }
}
