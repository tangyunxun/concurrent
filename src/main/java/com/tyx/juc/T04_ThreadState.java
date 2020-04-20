package com.tyx.juc;

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
 */
public class T04_ThreadState {

    public static class MyThread extends Thread{

        @Override
        public void run(){
            System.out.println(this.getState());
        }
    }

    public static void main(String[] args){

        Thread t = new MyThread();

        System.out.println(t.getState());

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(t.getState());
    }
}
