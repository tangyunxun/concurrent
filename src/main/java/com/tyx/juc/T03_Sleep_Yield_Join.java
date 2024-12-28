package com.tyx.juc;

/**
 * @Author Axun
 * @Create 2020/4/20 9:57
 * sleep: 当前线程沉睡一段时间，并且让出cpu时间。线程苏醒后回到就绪状态，但不保证立马可以运行。
 * yield: 线程让步。当前线程让出cpu，回到就绪状态，继续参与对于cpu的竞争，并且有可能竞争获胜再次获得cpu时间
 * join : 在A线程里，ThreadB.join()，则等待ThreadB执行完，A才继续执行。
 *
 */
public class T03_Sleep_Yield_Join {
    public static void main(String[] args){
        //testSleep();
        //testYield();
        testJoin();
    }

    static void testSleep() {
        new Thread(() -> {
            for(int i = 0; i < 10; i++){
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static void testYield() {
        new Thread(() -> {
            for(int i = 0; i < 100; i++){
                System.out.println("A" + i);
                if(i%10==0) {
                    Thread.yield();
                }
            }
        }).start();

        new Thread(() -> {
            for(int i = 0; i < 100; i++){
                System.out.println("------------B" + i);
                if(i%10==0) {
                    Thread.yield();
                }
            }
        }).start();
    }

    static void testJoin(){
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 100; i++){
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                for(int i = 0; i < 100; i++){
                    System.out.println("B" + i);
                    if(i==10){
                        t1.join();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

    }
}
