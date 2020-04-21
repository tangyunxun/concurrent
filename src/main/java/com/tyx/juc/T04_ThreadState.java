package com.tyx.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author Axun
 * @Create 2020/4/20 10:22
 * JDK源码注释，详细地介绍了6种线程状态：
 * NEW
 * RUNNABLE
 * BLOCKED
 * WAITING
 * TIMED_WAITING
 * TERMINATED
 *
 * 以下代码,基于一个定时任务线程池，验证了线程的六种状态
 */
public class T04_ThreadState {

    static volatile Integer i = 1;

    private static Object lock = new Object();

    private static Object lock2 = new Object();

    public static class MyThread extends Thread{
        @Override
        public void run(){
            System.out.println(this.getName() + " --- " + this.getState());

            try {
                //测试sleep(1000)时的状态
                System.out.println("sleep(1000) >>>>>>");
                Thread.sleep(1000);

                synchronized (lock){
                    i++;
                    System.out.println("wait() >>>>>>");
                    //测试wait()之后的状态
                    lock.wait();
                    i++;

                    System.out.println("wait(1000) >>>>>>");
                    //测试wait(1000)时的状态
                    lock.wait(1000);
                }
                System.out.println("synchronized (lock2) >>>>>>");
                //测试等待lock2时的状态
                synchronized (lock2){
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    //在Thread-A wait()之后，唤醒它
    static void notifyThreadA() throws InterruptedException {
        synchronized (lock2){
            while(i <= 2){
                Thread.sleep(1000);
                synchronized (lock){
                    lock.notify();
                }
            }
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread t = new MyThread();
        t.setName("Thread-A");

        //起一个定时任务的线程池，用来定时检测某线程的状态
        ExecutorService e = Executors.newScheduledThreadPool(1);
        ((ScheduledExecutorService) e).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("###"+t.getName() + " --- " + t.getState());
            }
        }, 0, 1, TimeUnit.SECONDS);

        new Thread(() -> {
            try {
                notifyThreadA();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }).start();

        Thread.sleep(600);
        t.start();
        t.join();
    }
}
