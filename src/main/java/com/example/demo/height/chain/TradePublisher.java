/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo.height.chain;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class TradePublisher implements Runnable {

    private Disruptor<Trade> disruptor;
    private CountDownLatch latch;
    private static int PUBLISH_COUNT = 10;

    public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.latch = latch;
        this.disruptor = disruptor;
    }

    @Override
    public void run() {

        TradeEventTranslator eventTranslator = new TradeEventTranslator();

        for(int i = 0; i < PUBLISH_COUNT; i++) {
            // 新的提交方式
            disruptor.publishEvent(eventTranslator);
        }
        latch.countDown();
    }
}

class TradeEventTranslator implements EventTranslator<Trade> {

    private Random random = new Random();

    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private void generateTrade(Trade event) {
        event.setPrice(random.nextDouble() * 9999);
    }
}
