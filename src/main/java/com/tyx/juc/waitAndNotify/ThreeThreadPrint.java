package com.tyx.juc.waitAndNotify;

/**
 * @Author Axun
 * @Create 2020/4/21 19:17
 *
 * 三个线程依次分别打印A、B、C，循环10次
 *
 * 可扩展至任意多个线程的循环依次执行，
 * 核心思想就是一个公共变量flag的赋值来控制多个线程的执行顺序
 */
public class ThreeThreadPrint {

    private static Integer flag = 1;

    synchronized void printA(){
        for(int i = 0; i < 10; i++){
            while(flag != 1){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.notifyAll();
            //doSomeThing
            System.out.println("A");
            flag = 2;
        }
    }
    synchronized void printB(){
        for(int i = 0; i < 10; i++){
            while(flag != 2){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.notifyAll();
            //doSomeThing
            System.out.println("B");
            flag = 3;
        }
    }
    synchronized void printC(){
        for(int i = 0; i < 10; i++){
            while(flag != 3){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.notifyAll();
            //doSomeThing
            System.out.println("C");
            flag = 1;
        }
    }

    public static void main(String[] args){
        ThreeThreadPrint obj = new ThreeThreadPrint();
        new Thread(() -> {
            obj.printA();
        }, "Thread-A").start();
        new Thread(() -> {
            obj.printB();
        }, "Thread-B").start();
        new Thread(() -> {
            obj.printC();
        }, "Thread-C").start();
    }
}
