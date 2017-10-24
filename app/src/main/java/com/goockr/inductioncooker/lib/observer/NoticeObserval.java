package com.goockr.inductioncooker.lib.observer;

/**
 * Created by LJN on 2017/10/19.
 */

public interface NoticeObserval {
    /**
     * 注册观察者
     */
    void registerObserver(NoticeObserver observer);

    /**
     * 移除观察者
     */
    void removeObserver(NoticeObserver observer);

    /**
     * 通知观察者
     */
    void notifyObservers(String succeedStr);
}
