/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 例子：核心的 Disruptor 如何来写
 * Disruptor 编程模型
 *
 * @author study
 * @date 2019-09-07
 */
public class Main {

    public static void main(String[] args) {


        OrderEventFactory orderEventFactory = new OrderEventFactory();

        int ringBufferSize = 1024 * 1024;

        // 1. 实例化 disruptor 对象
        Disruptor<OrderEvent> disruptor = new Disruptor<>(orderEventFactory,
                                                          // 容器大小
                                                          ringBufferSize,
                                                          // 创建线程的工厂方法
                                                          Executors.defaultThreadFactory(),
                                                          // 单生产者模式
                                                          ProducerType.SINGLE,
                                                          // 消费者等待策略
                                                          new BlockingWaitStrategy());

        // 2. 添加消费者监听，构建 disruptor 与消费者的一个关联关系。
        disruptor.handleEventsWith(new OrderEventHandler());

        // 3. 启动disruptor
        // 3和4可以合起来 RingBuffer<OrderEvent> ringBuffer = disruptor.start();
        disruptor.start();

        // 4. 获取实际存储数据的容器（RingBuffer 实际存储数据的角色）
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        OrderEventProducer producer = new OrderEventProducer(ringBuffer);

        // 初始化字节长度
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for(long i = 0; i < 100; i ++) {
            byteBuffer.putLong(0, i);
            // 5. 生产者生产消息（event）
            producer.sendData(byteBuffer);
        }

        // 6. 优雅关闭
        disruptor.shutdown();

    }


}
