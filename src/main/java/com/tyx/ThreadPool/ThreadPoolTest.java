package com.tyx.ThreadPool;

import com.alibaba.fastjson.JSON;
import javafx.concurrent.Task;

import java.util.concurrent.FutureTask;
import java.util.concurrent.*;

/**
 * @Author Axun
 * @Create 2020/3/30 11:08
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        System.out.println("主线程---"+Thread.currentThread().getName());
        ExecutorService executor = new myThreadPool(1, 1, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1));
        ((ThreadPoolExecutor) executor).setRejectedExecutionHandler(new myRejectHandler());
        for (int i = 0; i < 1; i++) {
            executor.execute((new MyRun(3)));
        }
    }

    public static class myThreadPool extends ThreadPoolExecutor{

        public myThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }


        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if(t!=null){
                System.out.println(t.getMessage());
                String count = t.getMessage().substring(t.getMessage().lastIndexOf('=')+1, t.getMessage().length());
                System.out.println("count="+count);
                if(Integer.valueOf(count) >= 0){
                    execute(r);
                }
            }
        }
    }

    public static class MyRun implements Runnable {
        public MyRun(int n){
            this.count = n;
        }
        Integer count;
        @Override
        public void run() {
            count--;
            System.out.println("thread name is: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("before");
            throw new RuntimeException("剩余次数="+count);
            //return null;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }


    static class myRejectHandler implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("丢弃任务,当前线程---"+Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

