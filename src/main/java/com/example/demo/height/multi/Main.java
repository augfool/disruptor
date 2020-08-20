/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo.height.multi;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 多生产者，多消费者
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Main {

    public static void main(String[] args) throws Exception{

        // 构建一个线程池用于提交任务
        ThreadPoolExecutor executor = new ThreadPoolExecutor(32, 32,
                                                             60L, TimeUnit.SECONDS,
                                                             new ArrayBlockingQueue<>(1000));


        // 1、创建ringBuffer
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                                                         () -> new Order(),
                                                         1024 * 1024,
                                                         new YieldingWaitStrategy()
                                                        );

        // 2、通过ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 3、创建多消费者数组
        Consumer[] consumers = new Consumer[10];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C" + i);
        }

        // 4、构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<>(ringBuffer,
                                                             sequenceBarrier,
                                                             new EventExceptionHandler(),
                                                             consumers);

        // 5、设置多个消费者的sequence序号用于单独统计消费进度，并且设置到ringBuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 6、启动workerPool
        workerPool.start(executor);

        //*********
        //7、创建生产者
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for(int i = 0; i < 100; i++) {
            Producer producer = new Producer(ringBuffer);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                       // 所有线程都在这阻塞这
                       countDownLatch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 每个线程都执行100次
                    for(int j = 0; j < 100; j++) {
                        producer.sendData(UUID.randomUUID().toString());
                    }
                }
            }).start();
        }

        countDownLatch.countDown();
        System.out.println("-------------------线程创建完毕，开始成产数据------------------------");

        // 等待一段时间，生产和消费数据
        Thread.sleep(5000);
        System.out.println("任务总数：" + consumers[2].getCount());

    }

    static class EventExceptionHandler implements ExceptionHandler<Order> {

        @Override
        public void handleEventException(Throwable ex, long sequence, Order event) {

        }

        @Override
        public void handleOnStartException(Throwable ex) {

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {

        }
    }

}
