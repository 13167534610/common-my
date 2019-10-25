package com.common.funciton.paramvalid;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/3/11 16:02
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface ParamNotNull {
    String message() default "";

}
