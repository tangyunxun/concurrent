package com.tyx.juc;

import java.util.concurrent.TimeUnit;

/**
 * @Author Axun
 * @Create 2020/4/20 13:51
 *
 * 线程安全性：当多个线程访问某个类时，这个类始终能表现出正确的行为，那么称该类为线程安全的。
 *
 * 竟态条件：由于不恰当的执行顺序而出现不正确的结果的情况，也就是说正确的结果取决于运气。
 * 最常见的竟态条件类型就是“先检查后执行”（Check-Then-Act），通过一个可能失效的观测结果来决定下一步的操作。
 *
 * 虚拟机规范并没有规定synchronized的实现细节，HOTSPOT是通过对象头里的两位标志加锁状态。
 * 非静态方法中锁定的是本对象，静态方法中锁定的是对应的Class对象
 *
 * Synchronized不要锁定String常量、Integer、Long等
 *
 * synchronized是可重入锁。举例：
 * synchronized m1(){
 *      m2();
 * }
 * synchronized m2()
 *
 * 发生异常会释放锁
 *
 * HOTSPOT的Synchronized底层实现：
 * JDK早期实现是非常重量级的，每次都要去和OS申请锁
 * 后来改进
 * 三步锁升级   参考  《我是厕所所长》（一 二）
 * 1.偏向锁。对于一个同步代码块，第一个线程到来，只是在markword记录下这个线程的id，并没有真正加锁（此时是偏向锁），效率高
 * 2.轻量级锁。如果有线程争用，此时升级为自旋锁（占cpu时间，但是始终运行在用户态，不经过内核）
 * 3.重量级锁。自旋锁旋转一定次数（一般10次）之后，如果还拿不到锁，升级为重量级锁->去OS内核申请资源加锁（重量级锁处于阻塞状态。不占用cpu时间）
 *
 * 目前只能升级，不能降级，但是可以实现为可降级。
 *
 *
 * 自旋锁和系统锁（重量级锁）选型：
 * 加锁的代码执行时间短（自旋10次能拿到锁），线程数目不能太长，用自旋锁
 * 执行时间长，线程多，用系统锁
 * 
 * 锁优化：
 * 一般情况下，锁的粒度要尽量细。但是过于细，过多的锁可能会使得锁竞争太激烈，这时候要适当的粗化锁。
 * 锁住的对象要是final的
 */
public class T05_Synchronized {
    private static int count = 10;
    /**
     * 所对象应该是final的，想想为什么？
     */
    private final Object o = new Object();

    public static void main(String[] args){
        for(int i = 0; i < 10; i++){
            new SyncTest().start();
        }
    }
    public void m1(){
        synchronized(o){
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }
    public static class SyncTest extends Thread {
        @Override
        public synchronized void run(){//复合操作需要同步
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }


}

/**
 * 模拟银行账户，对业务写方法进行加锁，业务读方法不加锁，这样行不行？
 * Answer: 不行。
 * 首先可以看出，一个类的同步方法和非同步方法是可以被同时调用的（读、写方法）
 * 正是基于此
 * 由于set()中间sleep2000ms，导致read()拿到了没被赋值的count，属于脏读。
 */
class AccountDemo {
    private String name;
    private int count;

    public synchronized void set(String name, int count) {
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.count = count;
    }

    public int read(){
        return this.count;
    }

    public static void main(String[] args){
        AccountDemo account = new AccountDemo();
        new Thread(() -> {
            account.set("ZhangSan", 10000);
        }).start();

        new Thread(() -> {
            System.out.println(account.read());
        }).start();
    }

}

/**
 * 程序在执行过程中，如果抛出异常，默认情况下会释放锁。
 * 所以在并发情况下，有异常要多加小心，不然有可能会发生不一致的情况
 * 比如在一个web app处理过程中，多个servlet线程共同访问一个资源，这时如果出现异常处理不合适，
 * 在第一个线程中抛出异常，其他线程会进入同步代码区，有可能会访问到异常产生时的数据
 * 因此要非常小心处理同步业务逻辑中的异常
 */
class ExceptionLockDemo{
    int count = 0;
    synchronized void m(){
        System.out.println(Thread.currentThread().getName() + " start");
        while(true){
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if( count == 5){
                int i = 1/0;//此处抛出除0异常，锁将被释放，若想不被释放，可以在这里进行catch，让循环继续。
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args){
        ExceptionLockDemo e = new ExceptionLockDemo();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                e.m();
            }
        };
        new Thread(r, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        new Thread(r, "t2").start();
    }
}


