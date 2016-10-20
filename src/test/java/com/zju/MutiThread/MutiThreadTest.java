package com.zju.MutiThread;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2016/10/19.
 */
public class MutiThreadTest {
    public static void main(String[] args) {
//        testThread();
//        testsynchronized();
//        testBlockingQueue();
//        testThreadLocal();
        testExecutor();

    }

    public static void testThread(){
        for (int i=0;i<5;i++){
            MyThread myThread=new MyThread(i);
//            myThread.start();//这里就同时开了五个线程哦


            //线程3的实现
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<10;i++){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(String.format("当前线程名字：%s  当前线程打印数字:%d",MyThread.currentThread().getName(),i));
                    }
                }
            });

        }
    }

    //演示同步锁
    private static Object obj=new Object();
    public static void testsynchronized1(){
        synchronized (obj){
            for (int i=0;i<4;i++){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("执行test1：%s  当前线程打印数字:%d",MyThread.currentThread().getName(),i));
            }
        }
    }

    public static void testsynchronized2(){
        synchronized (obj){
            for (int i=0;i<4;i++){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("执行test2：%s  当前线程打印数字:%d",MyThread.currentThread().getName(),i));
            }
        }
    }


    public static void testsynchronized(){
        for (int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testsynchronized1();
                    testsynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue(){
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(20);
        Producer p = new Producer(q);
        Consumer c1 = new Consumer(q);
        Consumer c2 = new Consumer(q);
        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();

    }

    private static int userId;
    private static ThreadLocal<Integer> threadLocalUserIds =new ThreadLocal<>();

    public static void testThreadLocal(){
        for (int i=0;i<10;i++){//起了10条线程
            final  int finalId=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    threadLocalUserIds.set(finalId);
                    System.out.println("ThreadLocal:"+threadLocalUserIds.get());
                    Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExecutor(){
//        ExecutorService service= Executors.newSingleThreadExecutor();
        ExecutorService service= Executors.newFixedThreadPool(6);

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    try {
                        System.out.println("Executor1:"+i+"***********"+Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        //提交第二个任务
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    try {
                        System.out.println("Executor2:"+i+"***********"+Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

}

class MyThread extends Thread{
    private int tid;
    public MyThread(int id){
        this.tid=id;
    }

    //run里面是线程需要做的事情 :执行
    @Override
    public void run() {
       try{
            for (int i=0;i<10;i++){
                Thread.sleep(2000);
                System.out.println(String.format("当前线程名字：%s当前线程id:%d,当前线程打印数字:%d",MyThread.currentThread().getName(),tid,i));
            }
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
}

class MyThread2 implements Runnable{
    @Override
    public void run() {
        try{
            for (int i=0;i<10;i++){
                Thread.sleep(100);
                System.out.println(String.format("当前线程名字：%s  当前线程打印数字:%d",MyThread.currentThread().getName(),i));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}


//synchronized

//下面演示BlockingQueue，主要用于生产者--使用者的队列

class Consumer implements  Runnable{
    private  BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        try {
            //这是一个消费线程；卡住来取
            while (true){
                System.out.println("consumer:"+Thread.currentThread().getName()+":"+q.take());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

class Producer implements Runnable {
    private  BlockingQueue<String> q;
    Producer(BlockingQueue q) {
        this.q = q;
    }
    public void run() {
        try {
            while(true) {
                for (int i=0;i<30;i++){
                    Thread.sleep(100);
                    q.put(String.valueOf(i));
                    System.out.println("producer:"+Thread.currentThread().getName()+": 添加了"+i);

                }
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}





