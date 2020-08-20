/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo.height.multi;

import com.lmax.disruptor.RingBuffer;

/**
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Producer {

    private RingBuffer<Order> ringBuffer;

    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid) {

        long sequence = ringBuffer.next();
        try{
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }

    }
}
