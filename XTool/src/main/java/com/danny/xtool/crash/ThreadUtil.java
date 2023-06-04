/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.crash;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadUtil {
    /**
     * 重入锁
     * 同一时刻只有一个线程进入临界区
     * 临界区：同一时刻只有一个任务访问的代码区
     */
    private void reentrantLock() {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {

        } finally {
            lock.unlock();
        }
    }
}
