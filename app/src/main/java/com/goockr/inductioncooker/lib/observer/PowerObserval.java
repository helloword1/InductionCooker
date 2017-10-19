package com.goockr.inductioncooker.lib.observer;

/**
 * Created by LJN on 2017/10/19.
 */

public interface PowerObserval {
    /**
     * 注册观察者
     */
    void registerObserver(PowerObserver observer);

    /**
     * 移除观察者
     */
    void removeObserver(PowerObserver observer);

    /**
     * 通知观察者
     */
    void notifyObservers(String succeedStr);
}
