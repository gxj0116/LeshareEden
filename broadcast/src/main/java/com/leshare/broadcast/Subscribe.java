package com.leshare.broadcast;

import com.leshare.broadcast.inner.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：gxj on 2017/9/5 14:28
 * 邮箱：jun18735177413@sina.com
 *
 * @Description: 接收事件注解，必须在接收事件地方定制该注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.MAIN_THREAD;
}
