package com.tyx.juc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Axun
 * @Create 2020/4/23 14:20
 *
 * 两个特征：
 * 1. 保证线程可见性
 * 2. 禁止指令重排序
 *
 * 线程可见性基于MESI协议（有兴趣可以了解下）
 * 通过内存屏障（cpu指令）支撑实现禁止指令重排序
 */
public class T06_Volatile {

    private static boolean flag = true;

    public static void main(String[] args){
        new Thread(() -> {
            while(flag){
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
            System.out.println("end.");
        }).start();

        flag = false;
    }
}

/**
 * 双重检查用到volatile
 * volatile禁止其修饰的对象指令重排序
 *
 * 创建对象的三大步骤：分配内存空间、给变量赋值、把引用指向该对象
 * 如果二、三步骤被重排序，颠倒了，就会造成未初始化完全的对象被释放出去。
 *
 */
class singleton{

    private static volatile Object obj;

    private singleton(){}

    private static Object getInstance(){
        if(obj == null){
            synchronized (obj){
                if(obj == null){
                    obj = new Object();
                }
            }
        }
        return obj;
    }
}


/**
 * volatile不能保证多个线程并发修改共享变量时的线程安全性
 * 所以volatile不能代替synchronized
 *
 * 运行下面代码，在加synchronized和不加synchronized两种情况下分析结果。
 */
class VolatileAndSynchronized{

    volatile int count = 0;

    //这里要不要加synchronized？
    private /*synchronized*/ void m(){
        for(int i = 0; i < 10000; i++){
            count++;
        }
    }

    public static void main(String[] args){
        VolatileAndSynchronized vas = new VolatileAndSynchronized();

        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            threads.add(new Thread(vas::m, "Thread--"+i));
        }
        threads.forEach((t) -> t.start());
        threads.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(vas.count);
    }
}
