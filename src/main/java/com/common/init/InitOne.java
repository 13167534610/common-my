package com.common.init;

import com.common.funciton.SysCacheUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/28 18:19
 */
@Order(1)
public class InitOne implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        SysCacheUtil.setCache("name", "lisi");
        System.out.println("========================第一个初始化器=============================");
    }
}
