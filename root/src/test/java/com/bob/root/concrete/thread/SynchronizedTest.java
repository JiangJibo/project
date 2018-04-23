package com.bob.root.concrete.thread;

import org.junit.Test;

/**
 * {@link Object#wait()} {@link Object#notify()}等方法必须在 "synchronized" 代码块中
 * 因为重量级锁的唤醒和等待需要使用 "Monitor" 当不在 "synchronized" 中,其方法就不会有效果
 * 能够正常执行wait和notify的前提是当前线程取得了锁
 *
 * 当在方法中时,会尝试从锁对象的 "Monitor" 中寻找当前线程的id,修改器状态
 * {@link Object#wait()}: 将 "Monitor" 中持有锁的线程id清空,同时释放锁
 * {@link Object#notify()}： 唤醒 "Monitor" 候选线程中的一个
 * 若不存在当前threadId, 会抛出 {@link IllegalMonitorStateException}
 *
 * @author Administrator
 * @create 2018-04-22 21:49
 */
public class SynchronizedTest {

    private Object lock = new Object();

    /**
     * @throws InterruptedException
     */
    @Test
    public void testWait() throws InterruptedException {
        this.waitInSync();
    }

    @Test
    public void testNotify() throws InterruptedException {
        synchronized (lock) {
            notifyInSync();
        }
        waitInSync();
    }

    public void waitInSync() throws InterruptedException {
        lock.wait();
    }

    public void notifyInSync() throws InterruptedException {
        lock.notify();
    }

}
