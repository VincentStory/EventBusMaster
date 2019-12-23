package com.netease.neeventbus.neeventbus;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，jvm在加载时可以通过反射的方式获取该注解的内容
public @interface Subscribe {

    ThreadMode threadMode() default ThreadMode.MAIN;
}
