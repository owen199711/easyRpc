package com.jia.easyRpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 定义注解：标识服务提供者，暴露服务接口
 * 通过该注解，表明暴露在外面的远程服务接口
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceExpose {
    String value() default "";
}
