/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Disruptor VS ArrayBLockingQueue
 * @author Kongjianfu
 * @date 2020-01-14
 */
public class Benchmark {

    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        benchmark.arrayBlockingQueueTest();
        benchmark.disruptorTest();
    }

    void arrayBlockingQueueTest() {
        ArrayBlockingQueue<Data> queue = new ArrayBlockingQueue<Data>(100000000);
        long startTime = System.currentTimeMillis();

        Runnable putRunnable = () -> {
            long i = 0;
            while (i < Constants.EVENT_NUM_OM) {
                Data data = new Data(i, "c" + i);
                queue.add(data);
                i ++;
            }
        };
        new Thread(putRunnable).start();

        Runnable takeRunnable = () -> {
            long k = 0;
            while (k < Constants.EVENT_NUM_OM) {
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                k ++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("ArrayBlockingQueue costTime = " + (endTime - startTime) + "ms");
        };
        new Thread(takeRunnable).start();
    }

    void disruptorTest() {
        int ringBufferSize = 65536;
        Disruptor<Data> disruptor = new Disruptor<Data>(new EventFactory<Data>() {
                                                            @Override
                                                            public Data newInstance() {
                                                                return new Data();
                                                            }
                                                        },
                                                        ringBufferSize,
                                                        Executors.defaultThreadFactory(),
                                                        ProducerType.SINGLE,
                                                        // new BlockingWaitStrategy()
                                                        new YieldingWaitStrategy());

        DataConsumer consumer = new DataConsumer();
        // 消费数据
        disruptor.handleEventsWith(consumer);
        disruptor.start();

        // 生产数据
        Runnable runnable = () -> {
            RingBuffer<Data> ringBuffer = disruptor.getRingBuffer();
            for (long i = 0; i < Constants.EVENT_NUM_OM; i++) {
                long seq = ringBuffer.next();
                Data data = ringBuffer.get(seq);
                data.setId(i);
                data.setName("c" +i);
                ringBuffer.publish(seq);
            }
        };
        new Thread(runnable).start();
    }
}

class DataConsumer implements EventHandler<Data> {

    private long startTime;
    private int i;

    public DataConsumer() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(Data event, long sequence, boolean endOfBatch) throws Exception {
        i++;
        if (i == Constants.EVENT_NUM_OM) {
            long endTime = System.currentTimeMillis();
            System.out.println("Disruptor constTime = " + (endTime - startTime) + "ms");
        }

    }
}

interface Constants {

    int EVENT_NUM_OHM = 100000000;

    int EVENT_NUM_FM = 50000000;

    int EVENT_NUM_OM = 10000000;

}

class Data {

    private Long id;
    private String name;

    public Data(){}

    public Data(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
