/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
 * @author study
 * @date 2019-09-07
 */
public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     *
     * @param data nio ByteBuffer
     */
    public void sendData(ByteBuffer data) {

        // 核心，也是比较难理解的地方

        // 1 在生产者发送消息的时候，首先需要从我们的ringBuffer里面，获取一个可用的序号。

        /**
         * 此处有坑，SingleProducerSequencer#next(int)
         * 当生产者速率 > 消费者速率，ringBuffer中没有位置，生产者需要等待时，会循环等待1纳秒，造成CPU空转。
         * 实际解决方法使用 SingleProducerSequencer#tryNext() 抛异常，代码例子：
         *
         *         long sequeue ;
         *         while (true){
         *             try{
         *                 sequeue = ringBuffer.tryNext() ;
         *                 break;
         *             }catch (Exception e){
         *                 log.info("produce ondata error:{}",e.getMessage());
         *                 try {
         *                     Thread.sleep(800);
         *                 }catch (Exception e1){
         *                 }
         *             }
         *
         *         }
         */
        long sequence = ringBuffer.next();

        // 官方 try{} finally{} 优雅的写法
        try {
            // 2 根据这个序号，找到具体的 "OrderEvent（空对象）" 元素，注意：此时获取的OrderEvent对象是一个没有被赋值的 "空对象"。
            OrderEvent event = ringBuffer.get(sequence);
            // 3 进行实际的赋值
            event.setValue(data.getLong(0));
        } finally {
            // 4 提交发布操作，发布这个序号
            ringBuffer.publish(sequence);
        }
    }
}
