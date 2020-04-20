package com.tyx.syncronizationTool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * @Author Axun
 * @Create 2019/12/5 19:47
 */
public class TestCyclicBarriar {
    public static void main(String [] args){
        CyclicBarrier barrier = new CyclicBarrier(20, () -> System.out.println("满人"));

        for(int i = 0; i < 100; i++){
            new Thread(()-> {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
