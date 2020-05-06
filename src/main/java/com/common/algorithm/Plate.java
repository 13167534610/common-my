package com.common.algorithm;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/1/9 14:13
 */
public class Plate<T> {

    private T t;

    public Plate() {
    }

    public Plate(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public static void main(String[] args) {
        Plate<? extends Fruit> applePlate = new Plate<>(new Banana());
        Fruit t = applePlate.getT();
        applePlate.getT().printName();

        Fruit f = new Apple();
        f.printName();

        Object o = new Object();
        o.getClass();
    }
}
