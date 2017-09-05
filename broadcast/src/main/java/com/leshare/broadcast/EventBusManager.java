package com.leshare.broadcast;

/**
 * 作者：gxj on 2017/9/5 14:22
 * 邮箱：jun18735177413@sina.com
 */
public class EventBusManager {
    private static IEventBus mInnerEventBus;
    private static IEventBus mExternalEventBus;

    public static void setBus(IEventBus bus) {
        if (mExternalEventBus == null && bus != null) {
            mExternalEventBus = bus;
        }
    }

    public static IEventBus getBus() {
        if (mInnerEventBus == null) {
            synchronized (EventBusManager.class) {
                if (mInnerEventBus == null) {
                    if (mExternalEventBus != null) {
                        mInnerEventBus = mExternalEventBus;
                    } else {
                        mInnerEventBus = new RxBusImpl();
                    }
                }
            }
        }
        return mInnerEventBus;
    }
}
