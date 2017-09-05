package com.leshare.broadcast;

/**
 * 作者：gxj on 2017/9/5 14:23
 * 邮箱：jun18735177413@sina.com
 *
 * @Description: 事件总线接口
 */
public interface IEventBus {
    void register(Object object);

    void unregister(Object object);

    void post(IEvent event);

    void postSticky(IEvent event);
}
