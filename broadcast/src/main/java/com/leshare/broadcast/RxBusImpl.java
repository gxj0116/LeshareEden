package com.leshare.broadcast;

import com.leshare.broadcast.inner.EventBase;
import com.leshare.broadcast.inner.EventComposite;
import com.leshare.broadcast.inner.EventFind;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 作者：gxj on 2017/9/5 14:28
 * 邮箱：jun18735177413@sina.com
 */
public class RxBusImpl extends EventBase implements IEventBus {
    private ConcurrentMap<Object, EventComposite> mEventCompositeMap = new ConcurrentHashMap<>();

    /**
     * 注册事件监听
     * @param object
     */
    @Override
    public void register(Object object) {
        if (object == null) {
            throw new NullPointerException("Object to register must not be null.");
        }
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        EventComposite subscriberMethods = EventFind.findAnnotatedSubscriberMethods(object, compositeDisposable);
        mEventCompositeMap.put(object, subscriberMethods);

        if (!STICKY_EVENT_MAP.isEmpty()) {//如果有粘性事件，则发送粘性事件
            subscriberMethods.subscriberSticky(STICKY_EVENT_MAP);
        }
    }

    /**
     * 取消事件监听
     * @param object
     */
    @Override
    public void unregister(Object object) {
        if (object == null) {
            throw new NullPointerException("Object to register must not be null.");
        }
        EventComposite subscriberMethods = mEventCompositeMap.get(object);
        if (subscriberMethods != null) {
            subscriberMethods.getCompositeDisposable().dispose();
        }
        mEventCompositeMap.remove(object);
    }

    /**
     * 发送普通事件
     * @param event
     */
    @Override
    public void post(IEvent event) {
        SUBJECT.onNext(event);
    }

    /**
     * 发送粘性事件
     * @param event
     */
    @Override
    public void postSticky(IEvent event) {
        STICKY_EVENT_MAP.put(event.getClass(), event);
        post(event);
    }
}
