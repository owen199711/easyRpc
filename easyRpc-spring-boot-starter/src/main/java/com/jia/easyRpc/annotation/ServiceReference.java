package com.jia.easyRpc.annotation;

import java.lang.annotation.*;

/**
 * 定义注解：注入远端服务
 * 通过改注解，在本地注入远程服务的方法
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceReference {
}
