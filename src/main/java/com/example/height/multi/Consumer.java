/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.height.multi;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.WorkHandler;

/**
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Consumer implements WorkHandler<Order> {

    private String consumerId;

    private static AtomicInteger count = new AtomicInteger(0);

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    private Random random = new Random();

    @Override
    public void onEvent(Order event) throws Exception {
        Thread.sleep(random.nextInt(5));
        System.out.println("当前消费者: " + this.consumerId + ", 消费信息ID: " + event.getId());
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
