package com.common.dataconfig.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MultiDataSourceTransactional {
    /**
     * 事务管理器数组
     */
    String[] transactionManagers();
}
