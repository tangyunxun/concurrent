package com.tyx.juc;

/**
 * @Author Axun
 * @Create 2020/4/20 9:36
 * Question：启动线程的三种方式：
 * 1. 继承Thread类（Thread类也实现了Runnable接口）
 * 2. 实现Runnable接口
 * 3. 启动线程池（其实不能算一个单独的方式）
 * 综上三种，我认为其实本质只是一种，都是通过实现Runnable接口。
 */
public class T02_HowToCreateThread {

    public static class MyThread extends Thread{
        @Override
        public void run(){
            System.out.println("Hello MyThread!");
        }
    }

    public static class MyRun implements Runnable{
        @Override
        public void run() {
            System.out.println("Hello MyRun!");
        }
    }

    public static void main(String[] args){
        new MyThread().start();
        new Thread(new MyRun()).start();
        new Thread(() -> System.out.println("Hello Lambda!")).start();
    }
}
