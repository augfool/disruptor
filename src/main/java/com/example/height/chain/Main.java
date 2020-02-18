/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.height.chain;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 构建一个线程池用于提交任务
        ThreadPoolExecutor executor = new ThreadPoolExecutor(32, 32,
                                                                  60L, TimeUnit.SECONDS,
                                                                       new ArrayBlockingQueue<>(1000));

        // 1、构建Disruptor
        Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {
                                                                @Override
                                                                public Trade newInstance() {
                                                                    return new Trade();
                                                                }
                                                          },
                                                          // 容器大小
                                                          1024 * 1024,
                                                          // 创建线程的工厂方法
                                                          Executors.defaultThreadFactory(),
                                                          // 单生产者模式
                                                          ProducerType.SINGLE,
                                                          // 消费者等待策略
                                                          new BusySpinWaitStrategy());

        // 2、把消费者设置到 Disruptor#handleEventsWith
        // 2.1 串行操作
/**        disruptor.handleEventsWith(new Handler1())
                 .handleEventsWith(new Handler2())
                 .handleEventsWith(new Handler3());*/

        // 2.2 并行操作，可以有两种方法
/**     disruptor.handleEventsWith(new Handler1());
        disruptor.handleEventsWith(new Handler2());
        disruptor.handleEventsWith(new Handler3());*/

        // 或者
/**
        disruptor.handleEventsWith(new Handler1(), new Handler2(), new Handler3()); */

        // 2.3 菱形操作（一）多边形有向拓扑图，例如：handler1，handler2）-> handler3
/**        disruptor.handleEventsWith(new Handler1(), new Handler2())
                 .handleEventsWith(new Handler3());*/

        // 菱形操作（二）
/**        EventHandlerGroup<Trade> eventHandlerGroup = disruptor.handleEventsWith(new Handler1(), new Handler2());
        eventHandlerGroup.then(new Handler3());*/


        // 有向拓扑图，单消费者模式，每一个监听都提供一个线程，此处有五个监听，要创建5个线程。BatchEventProcessor
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();

        disruptor.handleEventsWith(h1, h4);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2, h5).handleEventsWith(h3);

        // 3、启动 disruptor
        RingBuffer<Trade> ringBuffer = disruptor.start();

        long begin = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(1);

        executor.submit(new TradePublisher(latch, disruptor));

        latch.await(); //阻塞，等提交完继续向下


        disruptor.shutdown();
        executor.shutdown();

        System.out.println("总耗时：" + (System.currentTimeMillis() - begin) );



    }

}
