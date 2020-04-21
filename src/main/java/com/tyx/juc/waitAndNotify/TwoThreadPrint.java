package com.tyx.juc.waitAndNotify;

/**
 * @Author Axun
 * @Create 2020/4/21 14:03
 * 两个线程交替打印
 */
public class TwoThreadPrint {

    private static Object obj = new Object();

    private static Integer i = 1;

    public static void main(String[] args){

        new Thread(() -> {
            printIncNum();
        }, "Thread-A").start();

        new Thread(() -> {
            printIncNum();
        }, "Thread-B").start();

    }

    public static void printIncNum(){
        while(true){
            synchronized (obj){
                System.out.println(Thread.currentThread().getName() + " count = " + i++);
                obj.notify();
                try {
                    obj.wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
