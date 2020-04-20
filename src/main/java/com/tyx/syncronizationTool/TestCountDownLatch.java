package com.tyx.syncronizationTool;

/*CountDownLatch可以用来保证一个或者多个线程阻塞在某处，待某些其他指令执行完毕后再开始执行*/

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {

    public static void main(String [] args) throws Exception {
        int actorNum = 5;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(actorNum);
        for(int i = 0; i < actorNum; i++){
            new Thread(new Actor("Actor"+i, startSignal, doneSignal)).start();
        }
        System.out.println("各部门注意，演员请就位，Action~~~");//导演喊Action，各演员开始演戏
        startSignal.countDown();
        System.out.println("拍戏进行中");
        doneSignal.await();
        System.out.println("咔~~~");//导演喊咔，本场演完
    }
}

class Actor implements Runnable {
    private String name;
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    Actor(String name, CountDownLatch startSignal, CountDownLatch doneSignal){
            this.name = name;
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    public void run() {
        try{
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException ie) {

        }
    }
    void doWork(){
        System.out.println(name + " is performing...");
    }
}

