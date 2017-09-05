package com.leshare.broadcast.inner;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：gxj on 2017/9/5 14:28
 * 邮箱：jun18735177413@sina.com
 *
 * @Description: 事件线程
 */
public enum ThreadMode {
    MAIN_THREAD,
    NEW_THREAD,
    IO,
    SINGLE,
    COMPUTATION,
    TRAMPOLINE;

    public static Scheduler getScheduler(ThreadMode thread) {
        Scheduler scheduler;
        switch (thread) {
            case MAIN_THREAD:
                scheduler = AndroidSchedulers.mainThread();
                break;
            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;
            case IO:
                scheduler = Schedulers.io();
                break;
            case SINGLE:
                scheduler = Schedulers.single();
                break;
            case COMPUTATION:
                scheduler = Schedulers.computation();
                break;
            case TRAMPOLINE:
                scheduler = Schedulers.trampoline();
                break;
            default:
                scheduler = AndroidSchedulers.mainThread();
                break;
        }
        return scheduler;
    }
}
