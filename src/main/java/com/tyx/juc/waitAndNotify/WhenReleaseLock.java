package com.tyx.juc.waitAndNotify;

/**
 * @Author Axun
 * @Create 2020/4/21 17:05
 * 这个示例用于测试wait何时释放锁。
 * 其实wait()方法之后，立即释放锁，当前Thread进入WAITING状态，
 * 该线程被唤醒之后，从wait()方法之后的代码开始执行。
 *
 * 可以看到，当多个线程同时在争用一个锁，
 * 若每个线程都是先调用wait()，后调用notify()，
 * 那么所有的线程将进入WAITING状态，
 * 所以应该至少保证有一个线程先调用notify()，后调用wait()
 * 笔者认为最科学的写法，就是所有的线程先调用notify()，后wait()，没毛病。
 */
public class WhenReleaseLock {

    private static Object obj = new Object();

    private static Integer i = 1;

    public static void main(String[] args){

        new Thread(() -> {
            while(true){
                synchronized (obj){
                    System.out.println(Thread.currentThread().getName() + "进入synchronized");
                    obj.notify();
                    try {
                        obj.wait();
                        System.out.println(Thread.currentThread().getName() + " restart");
                        Thread.sleep(2000);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 退出synchronized");
                }
            }
        }, "Thread-A").start();

        new Thread(() -> {
            while (true){
                synchronized (obj){
                    System.out.println(Thread.currentThread().getName() + "进入synchronized");
                    obj.notify();
                    System.out.println(Thread.currentThread().getName() + " i = " + i);
                    try {
                        obj.wait();
                        System.out.println(Thread.currentThread().getName() + " restart");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Thread-B").start();
    }
}
